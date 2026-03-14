import json
import os

def update_idioms():
    assets_path = 'assets/idioms.json'
    new_path = 'python/idioms-new.json'
    version_path = 'assets/version.json'

    # 1. 기존 데이터 로드
    with open(assets_path, 'r', encoding='utf-8') as f:
        existing_data = json.load(f)
        existing_words = {item['word'] for item in existing_data}

    # 2. 새 데이터 로드
    with open(new_path, 'r', encoding='utf-8') as f:
        new_data = json.load(f)

    # 3. 중복 제외하고 합치기
    added_count = 0
    for item in new_data:
        if item['word'] not in existing_words:
            existing_data.append(item)
            added_count += 1

    # 4. 결과 저장
    with open(assets_path, 'w', encoding='utf-8') as f:
        json.dump(existing_data, f, ensure_ascii=False, indent=2)

    # 5. 버전 업데이트 (앱이 새 데이터를 내려받도록 함)
    if os.path.exists(version_path):
        with open(version_path, 'r', encoding='utf-8') as f:
            version_info = json.load(f)
        
        version_info['version'] = version_info.get('version', 0) + 1
        
        with open(version_path, 'w', encoding='utf-8') as f:
            json.dump(version_info, f, ensure_ascii=False, indent=2)
        print(f"Version updated to: {version_info['version']}")

    print(f"Successfully added {added_count} new idioms to {assets_path}")
    print(f"Total idioms: {len(existing_data)}")

if __name__ == "__main__":
    update_idioms()
