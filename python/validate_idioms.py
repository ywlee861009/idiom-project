import json
import time
import random
import requests
from collections import OrderedDict

INPUT_PATH = "python/idioms.json"
OUTPUT_PATH = "assets/idioms.json"
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


def dedup_by_hanja(idioms):
    seen = OrderedDict()
    for item in idioms:
        hanja = item["hanja"]
        if hanja not in seen:
            seen[hanja] = item
    return list(seen.values())


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
    print(f"Loading idioms from {INPUT_PATH}...")
    idioms = load_idioms(INPUT_PATH)
    print(f"Loaded {len(idioms)} items.")

    deduped = dedup_by_hanja(idioms)
    removed_dups = len(idioms) - len(deduped)
    if removed_dups > 0:
        print(f"Removed {removed_dups} duplicates. Remaining: {len(deduped)}")
    else:
        print("No duplicates found.")

    verified = []
    skipped = []
    errors = []

    total = len(deduped)
    for i, item in enumerate(deduped, 1):
        hanja = item["hanja"]
        result, reason = check_hanja(hanja)

        if result is True:
            verified.append(item)
            status = "OK"
        elif result is False:
            skipped.append(item)
            status = f"SKIP ({reason})"
        else:
            errors.append(item)
            status = f"ERROR ({reason})"

        print(f"[{i:3d}/{total}] {item['word']} ({hanja}) -> {status}")

        # Random delay to avoid bot detection
        time.sleep(random.uniform(0.5, 1.0))

    print("\n" + "=" * 50)
    print(f"Verified : {len(verified)}")
    print(f"Skipped  : {len(skipped)}")
    print(f"Errors   : {len(errors)}")
    print("=" * 50)

    if skipped:
        print("\nSkipped items:")
        for item in skipped:
            print(f"  - {item['word']} ({item['hanja']})")

    if errors:
        print("\nError items (included in output):")
        for item in errors:
            print(f"  - {item['word']} ({item['hanja']})")

    # Include error items in output (network issue, not confirmed invalid)
    output = verified + errors

    with open(OUTPUT_PATH, "w", encoding="utf-8") as f:
        json.dump(output, f, ensure_ascii=False, indent=2)

    print(f"\nSaved {len(output)} items to {OUTPUT_PATH}")


if __name__ == "__main__":
    main()
