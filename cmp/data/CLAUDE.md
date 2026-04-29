## Realm 마이그레이션 규칙

- **`deleteRealmIfMigrationNeeded()` 사용 금지** — 프로덕션 사용자의 학습 데이터가 전부 삭제됨
- 스키마 변경(필드 추가/삭제/타입 변경) 시 반드시 아래 절차를 따른다:
  1. `IdiomDatabase.kt`의 `schemaVersion`을 +1 증가
  2. 새 필드에는 반드시 기본값 지정 (예: `var isBookmarked: Boolean = false`)
  3. 기본값이 있는 단순 필드 추가는 Realm Kotlin SDK가 자동 마이그레이션 처리
  4. 필드 삭제, 타입 변경 등 복잡한 변경은 `.migration { }` 블록 작성 필요
- 현재 스키마 버전: **2** (v2에서 `DailyRecordEntity` 추가됨)
