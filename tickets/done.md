# ✅ Done Tickets (히스토리)

이 문서는 구현 및 검증이 완료된 티켓들의 기록입니다.

---

### ID-05: Minimalist UI Base Components
- **Description**: 미니멀리즘 디자인 컨셉을 반영한 공통 UI 컴포넌트 개발.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :core:assembleDebug` SUCCESS
- **Results**:
    - [x] 'Extreme Minimalism' 테마 (Color, Typography) 설정 완료.
    - [x] `IdiomBaseCard`, `IdiomPrimaryButton` 공통 컴포넌트 구현 완료.
- **Priority**: High

---

### ID-03: Domain Model & Data Parsing
- **Description**: `idioms.json` 데이터를 앱 내부에서 사용할 수 있는 도메인 모델로 변환 및 저장소 구현.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :data:assembleDebug` SUCCESS
- **Results**:
    - [x] `Idiom` 모델 정의 및 `kotlinx-serialization` 적용.
    - [x] `AssetIdiomDataSource` 구현 및 Repository 바인딩 완료.
- **Priority**: High

---

### ID-01: Android Project Scaffolding
- **Description**: Jetpack Compose 기반의 멀티모듈 Kotlin 프로젝트 초기 셋업.
- **Completed Date**: 2026-03-08
- **Results**:
    - [x] Multi-module Structure 구축 및 MVI/Navigation 2.8.7 설정 완료.
- **Priority**: High

---

### ID-02: Create Idiom Data Set (JSON)
- **Description**: 최소 20개의 사자성어 데이터를 포함하는 JSON 파일 생성.
- **Completed Date**: 2026-03-07
- **Results**:
    - [x] `assets/idioms.json` 생성 완료 (200개 데이터).
- **Priority**: Medium
