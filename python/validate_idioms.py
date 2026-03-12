import json
import re
import time
import random
import os
from datetime import datetime
import requests

_DIR = os.path.dirname(os.path.abspath(__file__))
EXISTING_PATH = os.path.join(_DIR, "../assets/idioms.json")  # 기존 검증된 항목
NEW_PATH = os.path.join(_DIR, "idioms-new.json")             # 신규 추가 항목
VERSION_PATH = os.path.join(_DIR, "../assets/version.json")
LOG_DIR = os.path.join(_DIR, "update-logs")
API_URL = "https://hanja.dict.naver.com/api3/ccko/search?query={word}&m=pc&hid={hid}"

HEADERS = {
    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Accept": "application/json, text/plain, */*",
    "Accept-Language": "ko-KR,ko;q=0.9",
    "Referer": "https://hanja.dict.naver.com/",
}


def load_idioms(path):
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)


def dedup_and_report(idioms):
    seen_word = {}   # word -> index
    seen_hanja = {}  # hanja -> index
    result = []
    duplicates = []

    for i, item in enumerate(idioms):
        word = item["word"]
        hanja = item["hanja"]
        dup_reason = None

        if word in seen_word:
            dup_reason = f"word '{word}' duplicates index {seen_word[word]}"
        elif hanja in seen_hanja:
            dup_reason = f"hanja '{hanja}' duplicates index {seen_hanja[hanja]}"

        if dup_reason:
            duplicates.append((i, item, dup_reason))
        else:
            seen_word[word] = i
            seen_hanja[hanja] = i
            result.append(item)

    return result, duplicates


def fetch_naver(word, retries=1):
    """네이버 한자사전에서 단어 검색. (naver_hanja, naver_meaning, status) 반환."""
    hid = int(time.time() * 1000)
    url = API_URL.format(word=requests.utils.quote(word), hid=hid)
    for attempt in range(retries + 1):
        try:
            resp = requests.get(url, headers=HEADERS, timeout=10)
            resp.raise_for_status()
            data = resp.json()

            items = (
                data.get("searchResultMap", {})
                    .get("searchResultListMap", {})
                    .get("WORD", {})
                    .get("items", [])
            )
            if not items:
                return None, None, "검색 결과 없음"

            top = items[0]
            naver_hanja = top.get("expEntry", "").strip()
            means_list = (
                top.get("meansCollector", [{}])[0]
                   .get("means", [{}])
            )
            naver_meaning = means_list[0].get("value", "").strip() if means_list else ""

            return naver_hanja, naver_meaning, "ok"
        except requests.RequestException as e:
            if attempt < retries:
                time.sleep(2)
                continue
            return None, None, f"오류: {e}"
    return None, None, "오류: 최대 재시도 초과"


def main():
    log = []

    def p(msg=""):
        print(msg)
        log.append(msg)

    # 1. 기존 검증된 항목 로드
    p(f"{EXISTING_PATH} 에서 기존 사자성어 불러오는 중...")
    existing = load_idioms(EXISTING_PATH)
    p(f"기존 항목 {len(existing)}개 로드 완료.")

    # 2. 신규 항목 로드
    p(f"{NEW_PATH} 에서 신규 사자성어 불러오는 중...")
    new_idioms = load_idioms(NEW_PATH)
    p(f"신규 항목 {len(new_idioms)}개 로드 완료.")

    # 3. 중복 검사: existing + new 전체를 dedup에 넘기면
    #    existing 항목이 먼저 seen에 등록되고 new 항목 중 중복이 걸러짐
    _, duplicates = dedup_and_report(existing + new_idioms)
    # new 항목 인덱스는 existing 이후부터 시작
    existing_count = len(existing)
    new_duplicates = [(idx, item, reason) for idx, item, reason in duplicates if idx >= existing_count]

    if new_duplicates:
        p(f"\n신규 항목 중 기존과 중복 ({len(new_duplicates)}개):")
        for idx, item, reason in new_duplicates:
            m = re.match(r"(word|hanja) '(.+)' duplicates index (\d+)", reason)
            if m:
                kind = "단어" if m.group(1) == "word" else "한자"
                dup_reason_kr = f"{kind} '{m.group(2)}'이(가) 기존 #{m.group(3)}번 항목과 중복"
            else:
                dup_reason_kr = reason
            p(f"  [#{idx - existing_count}] {item['word']} ({item['hanja']}) <- {dup_reason_kr}")
    else:
        p("신규 항목 중 중복 없음.")

    # 중복 제거된 new 항목만 추출
    dup_indices = {idx for idx, _, _ in new_duplicates}
    to_validate = [
        item for i, item in enumerate(new_idioms)
        if (i + existing_count) not in dup_indices
    ]
    p(f"중복 {len(new_duplicates)}개 제거. 검증할 신규 항목: {len(to_validate)}개")

    # 4. 신규 항목만 API 검증
    updated_hanja = []
    updated_meaning = []
    not_found = []
    errors = []
    validated_new = []

    total = len(to_validate)
    for i, item in enumerate(to_validate, 1):
        orig_hanja = item["hanja"]
        naver_hanja, naver_meaning, status = fetch_naver(item["word"])

        if status == "ok":
            if naver_hanja != orig_hanja:
                updated_hanja.append((item["word"], orig_hanja, naver_hanja))
            item = dict(item)
            item["hanja"] = naver_hanja
            if naver_meaning:
                item["meaning"] = naver_meaning
                updated_meaning.append(item["word"])
            p(f"[{i:3d}/{total}] {item['word']} ({orig_hanja} -> {naver_hanja}) -> 업데이트")
            validated_new.append(item)
        elif "오류" in status:
            errors.append(item["word"])
            p(f"[{i:3d}/{total}] {item['word']} ({orig_hanja}) -> {status}")
        else:
            not_found.append(item["word"])
            p(f"[{i:3d}/{total}] {item['word']} ({orig_hanja}) -> {status}")

        # 봇 감지 방지를 위한 랜덤 딜레이
        time.sleep(random.uniform(1.0, 2.0))

    # 5. 기존 항목 + 검증된 신규 항목 합치기
    output = existing + validated_new

    p("\n" + "=" * 50)
    p(f"[요약]")
    p(f"  기존 항목  : {len(existing)}개")
    p(f"  신규 입력  : {len(new_idioms)}개")
    p(f"  중복 제거  : {len(new_duplicates)}개")
    p(f"  검증 대상  : {len(to_validate)}개")
    p(f"  검증 통과  : {len(validated_new)}개")
    p(f"  출력 항목  : {len(output)}개")
    p(f"  한자 수정  : {len(updated_hanja)}개")
    p(f"  검색 안됨  : {len(not_found)}개")
    p(f"  오류       : {len(errors)}개")
    p("=" * 50)

    if updated_hanja:
        p("\n[한자 수정 목록]")
        for word, orig, new in updated_hanja:
            p(f"  - {word}: {orig} → {new}")

    if not_found:
        p("\n[검색 결과 없어서 제외된 항목]")
        for word in not_found:
            p(f"  - {word}")

    with open(EXISTING_PATH, "w", encoding="utf-8") as f:
        json.dump(output, f, ensure_ascii=False, separators=(',', ':'))
    p(f"\n총 {len(output)}개 항목을 {EXISTING_PATH} 에 저장 완료.")

    with open(VERSION_PATH, "r", encoding="utf-8") as f:
        version_data = json.load(f)
    old_version = version_data["version"]
    version_data["version"] += 1
    with open(VERSION_PATH, "w", encoding="utf-8") as f:
        json.dump(version_data, f, ensure_ascii=False, indent=2)
    p(f"버전 업데이트: {old_version} -> {version_data['version']}")

    # 6. 로그 파일 저장
    os.makedirs(LOG_DIR, exist_ok=True)
    now = datetime.now().strftime("%Y%m%d-%H%M%S")
    log_path = os.path.join(LOG_DIR, f"{now}.md")
    with open(log_path, "w", encoding="utf-8") as f:
        f.write(f"# 사자성어 업데이트 로그\n\n")
        f.write(f"- 날짜: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
        f.write(f"- 버전: {old_version} → {version_data['version']}\n\n")
        f.write("```\n")
        f.write("\n".join(log))
        f.write("\n```\n")
    print(f"로그 저장: {log_path}")


if __name__ == "__main__":
    main()
