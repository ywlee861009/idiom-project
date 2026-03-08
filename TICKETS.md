# 🎫 Development Tickets

## [To Do]

### ID-01: Android Project Scaffolding
- **Description**: Jetpack Compose 기반의 Kotlin 프로젝트 초기 셋업.
- **Acceptance Criteria**:
    - [ ] `com.kero.idiom` 패키지 구조 생성.
    - [ ] `build.gradle (Kotlin DSL)` 파일 구성 및 Compose 라이브러리 추가.
    - [ ] 빈 `MainActivity.kt`에서 "Hello Idiom" 텍스트가 정상 출력되는지 확인.
    - [ ] `assets` 폴더 생성 및 `idioms.json` 파일 배치 준비.
- **Technical Notes**:
    - Jetpack Compose, Material 3 라이브러리 사용.
    - MVVM 패턴을 위한 폴더 구조 (data, domain, ui) 사전에 고민.
- **Priority**: High

### ID-02: Create Idiom Data Set (JSON)
- **Description**: 최소 20개의 사자성어 데이터를 포함하는 JSON 파일 생성.
- **Acceptance Criteria**:
    - [ ] `assets/idioms.json` 파일 생성.
    - [ ] `word`, `meaning`, `hanja`, `difficulty` 필드를 포함한 리스트 구성.
- **Priority**: Medium
