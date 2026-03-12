import json
import time
import random
import requests

INPUT_PATH = "python/idioms.json"
OUTPUT_PATH = "python/idioms-result.json"
API_URL = "https://hanja.dict.naver.com/api/search?query={hanja}&range=word&page=1&display=5"

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


def check_hanja(hanja, retries=1):
    url = API_URL.format(hanja=requests.utils.quote(hanja))
    for attempt in range(retries + 1):
        try:
            resp = requests.get(url, headers=HEADERS, timeout=10)
            resp.raise_for_status()
            data = resp.json()

            items = data.get("items", [])
            if not items:
                return False, "no_results"

            # Check if any result's hanja matches exactly
            for item in items:
                entry_hanja = item.get("hanja", "")
                # Strip whitespace and compare
                if entry_hanja.strip() == hanja.strip():
                    return True, "ok"

            return False, "no_match"
        except requests.RequestException as e:
            if attempt < retries:
                time.sleep(2)
                continue
            return None, f"error: {e}"
    return None, "error: max retries"


def main():
    print(f"{INPUT_PATH} 에서 사자성어 불러오는 중...")
    idioms = load_idioms(INPUT_PATH)
    print(f"총 {len(idioms)}개 항목 로드 완료.")

    deduped, duplicates = dedup_and_report(idioms)
    if duplicates:
        print(f"\n중복 항목 발견 ({len(duplicates)}개):")
        for idx, item, reason in duplicates:
            if "word" in reason:
                dup_reason_kr = reason.replace("word", "단어").replace("duplicates index", "번째 항목과 중복")
            else:
                dup_reason_kr = reason.replace("hanja", "한자").replace("duplicates index", "번째 항목과 중복")
            print(f"  [#{idx}] {item['word']} ({item['hanja']}) <- {dup_reason_kr}")
    else:
        print("중복 항목 없음.")
    print(f"중복 {len(duplicates)}개 제거. 남은 항목: {len(deduped)}개")

    verified = []
    skipped = []
    errors = []

    total = len(deduped)
    for i, item in enumerate(deduped, 1):
        hanja = item["hanja"]
        result, reason = check_hanja(hanja)

        if result is True:
            verified.append(item)
            status = "확인됨"
        elif result is False:
            skipped.append(item)
            status = f"건너뜀 ({reason})"
        else:
            errors.append(item)
            status = f"오류 ({reason})"

        print(f"[{i:3d}/{total}] {item['word']} ({hanja}) -> {status}")

        # 봇 감지 방지를 위한 랜덤 딜레이
        time.sleep(random.uniform(0.5, 1.0))

    print("\n" + "=" * 50)
    print(f"확인됨  : {len(verified)}개")
    print(f"건너뜀  : {len(skipped)}개")
    print(f"오류    : {len(errors)}개")
    print("=" * 50)

    if skipped:
        print("\n건너뜀 항목:")
        for item in skipped:
            print(f"  - {item['word']} ({item['hanja']})")

    if errors:
        print("\n오류 항목 (결과에 포함됨):")
        for item in errors:
            print(f"  - {item['word']} ({item['hanja']})")

    # 오류 항목은 결과에 포함 (네트워크 문제이므로 유효하지 않다고 단정 불가)
    output = verified + errors

    with open(OUTPUT_PATH, "w", encoding="utf-8") as f:
        json.dump(output, f, ensure_ascii=False, indent=2)

    print(f"\n총 {len(output)}개 항목을 {OUTPUT_PATH} 에 저장 완료.")


if __name__ == "__main__":
    main()
