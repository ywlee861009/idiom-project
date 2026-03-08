# ✅ Done Tickets (히스토리)

이 문서는 구현 및 검증이 완료된 티켓들의 기록입니다.

---

### ID-03: Domain Model & Data Parsing
- **Description**: `idioms.json` 데이터를 앱 내부에서 사용할 수 있는 도메인 모델로 변환 및 저장소 구현.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :data:assembleDebug` SUCCESS
- **Results**:
    - [x] `Idiom` 모델 정의 및 `kotlinx-serialization` 적용.
    - [x] `AssetIdiomDataSource` (Assets 로드 로직) 구현.
    - [x] `IdiomRepository` 인터페이스 및 구현체 작성.
    - [x] Hilt 의존성 주입 연동.
- **Priority**: High

---

### ID-01: Android Project Scaffolding
- **Description**: Jetpack Compose 기반의 멀티모듈 Kotlin 프로젝트 초기 셋업.
- **Completed Date**: 2026-03-08
- **Results**:
    - [x] Multi-module Structure (6 modules) 구축.
    - [x] MVI Architecture & Navigation 2.8.7 (Type-safe) 설정.
- **Priority**: High

---

### ID-02: Create Idiom Data Set (JSON)
- **Description**: 최소 20개의 사자성어 데이터를 포함하는 JSON 파일 생성.
- **Completed Date**: 2026-03-07
- **Results**:
    - [x] `assets/idioms.json` 생성 완료 (200개 데이터).
- **Priority**: Medium
