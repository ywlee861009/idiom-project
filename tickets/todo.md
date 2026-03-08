# 📋 To Do Tickets (백로그)

아직 시작하지 않은 작업 목록입니다. 담당자는 **Android 개발자**입니다.

---

### ID-01: Android Project Scaffolding
- **Assignee**: Android Developer
- **Description**: Jetpack Compose 기반의 Kotlin 프로젝트 초기 셋업.
- **Acceptance Criteria**:
    - [ ] `com.kero.idiom` 패키지 구조 생성.
    - [ ] `build.gradle (Kotlin DSL)` 파일 구성 및 Compose 라이브러리 추가.
    - [ ] 빈 `MainActivity.kt`에서 "Hello Idiom" 텍스트 정상 출력 확인.
    - [ ] `assets` 폴더 생성 및 `idioms.json` 파일 배치 준비.
- **Priority**: High

---

### ID-03: Domain Model & Data Parsing
- **Assignee**: Android Developer
- **Description**: `idioms.json` 데이터를 앱 내부에서 사용할 수 있는 도메인 모델로 변환.
- **Acceptance Criteria**:
    - [ ] `Idiom` 데이터 클래스 생성 (`word`, `hanja`, `meaning`, `level`).
    - [ ] JSON 파일을 읽어 `List<Idiom>`으로 변환하는 `IdiomDataSource` 구현.
    - [ ] 추후 DB 대체를 고려한 `IdiomRepository` 인터페이스 및 구현체 작성.
- **Priority**: High

---

### ID-04: Quiz Engine Algorithm
- **Assignee**: Android Developer
- **Description**: 퀴즈 생성 및 정답 판별 로직 구현.
- **Acceptance Criteria**:
    - [ ] 200개의 데이터 중 랜덤하게 문제를 추출하는 로직.
    - [ ] 4글자 중 랜덤하게 1글자를 비우는 로직 (예: 사자○어).
    - [ ] 오답 보기를 생성하거나 입력값과 정답을 비교하는 유효성 검사 로직.
- **Priority**: Medium

---

### ID-05: Minimalist UI Base Components
- **Assignee**: Android Developer
- **Description**: 미니멀리즘 디자인 컨셉을 반영한 공통 UI 컴포넌트 개발.
- **Acceptance Criteria**:
    - [ ] Material 3 테마 설정 (Color Palette: Soft White, Deep Navy, Light Grey).
    - [ ] 퀴즈용 카드 컴포넌트 (Typography 강조).
    - [ ] 심플한 정답 버튼 및 점수 표시용 텍스트 스타일 정의.
- **Priority**: Medium
