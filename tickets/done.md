# ✅ Done Tickets (히스토리)

이 문서는 구현 및 검증이 완료된 티켓들의 기록입니다.

---

### [BUG-001] 사자성어 길이 불일치로 인한 StringIndexOutOfBoundsException 해결
- **Completed Date**: 2026-03-13
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `GetRandomQuizUseCase` 내 하드코딩된 `(0 until 4)` 범위를 `(0 until idiom.word.length)`로 수정.
    - [x] 2글자(`계륵` 등), 3글자(`노익장` 등), 5글자(`가정맹어호` 등) 성어 출제 시 인덱스 에러 방지.
- **Priority**: Highest

---

### [TASK-023] 힌트 보기 광고 UX 개선 및 로딩 지연 해결
- **Completed Date**: 2026-03-11
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `QuizViewModel` 초기화 시 보상형 광고(Rewarded Ad) 미리 로딩(Pre-load) 로직 추가.
    - [x] `AdController` 인터페이스 및 `AndroidAdController` 구현체 확장 (로딩 상태 체크 및 리턴 타입 변경).
    - [x] `QuizScreen` 내 `SnackbarHost` 연동 및 광고 로딩 중일 때 실버 세대 맞춤형 피드백 UI 구현.
- **Priority**: High

---

### [TASK-021] Firebase SDK 및 Crashlytics/Analytics 연동
- **Completed Date**: 2026-03-11
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `libs.versions.toml`에 Firebase BoM(33.9.0), Analytics, Crashlytics 설정.
    - [x] 루트 및 `composeApp` 수준 `build.gradle.kts`에 `google-services` 및 `crashlytics` 플러그인 적용.
    - [x] `google-services.json` 파일을 `cmp/composeApp/` 모듈 루트로 배치하여 빌드 시스템 연동.
    - [x] `IdiomApplication`에서 Crashlytics 초기화 로그 및 수집 활성화 코드 추가.
- **Priority**: High

---

### ID-32: Google AdMob SDK 연동 및 기초 설정 (수익화 기초)
- **Completed Date**: 2026-03-11
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `libs.versions.toml` 및 `build.gradle.kts`에 AdMob SDK(`23.6.0`) 연동.
    - [x] `commonMain` 내 `AdController` 인터페이스를 통한 플랫폼 독립적 호출 구조 구축.
    - [x] `androidMain` 내 `AndroidAdController` 전면 광고(Interstitial) 로딩 및 노출 로직 구현.
    - [x] `MainActivity` 및 `IdiomApplication`에서 SDK 초기화 및 Koin DI 등록 완료.
- **Priority**: Highest

---

### ID-29: [Level 2] 주관식 2칸 채우기 퀴즈 개발
- **Completed Date**: 2026-03-11
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `QuizType` 확장 및 `GetRandomQuizUseCase` 내 2칸 빈칸 생성 로직 구현.
    - [x] `QuizViewModel`에 주관식 입력 처리(`InputAnswer`, `SubmitAnswer`) 로직 추가.
    - [x] `QuizScreen`에 실버 세대를 위한 대형 `OutlinedTextField` 주관식 UI 구현.
- **Priority**: High

---

### ID-30: [Level 3] 주관식 전체 채우기 퀴즈 개발
- **Completed Date**: 2026-03-11
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `FILL_BLANKS_4` 유형 지원 및 4글자 전체 빈칸 처리.
    - [x] 주관식 정답 검증 로직 공용화 및 정답 공개 기능 추가.
- **Priority**: Medium

---

### ID-25: 상세 UI 디자인 - Floating Tab & 3단 구조 (Jenny)
- **Completed Date**: 2026-03-10
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] 화면 하단 캡슐형 Floating Tab Bar 구현 및 `HomeScreen`, `CollectionScreen`, `ProfileScreen` 연동.
    - [x] 40대 이상 사용자를 고려한 'The Calm Ink' 디자인 시스템 확장 적용.
- **Priority**: Highest

---

### ID-20: Room DB 및 초기 동기화 (Smart Migration)
- **Completed Date**: 2026-03-10
- **Build Verified**: [x] `./cmp/gradlew :data:assembleDebug` SUCCESS
- **Results**:
    - [x] Room DB 및 `IdiomDao` 구현 완료.
    - [x] `assets/idioms.json` 버전 체크 기반 최초 실행/업데이트 시 자동 동기화 로직 구현.
- **Priority**: Highest

---

### ID-21: 지능적 퀴즈 추출 알고리즘 (Frequency-Based Random)
- **Completed Date**: 2026-03-10
- **Build Verified**: [x] `./cmp/gradlew :domain:assembleDebug` SUCCESS
- **Results**:
    - [x] `exposureCount ASC` 정렬 및 `RANDOM()` 기반의 Smart Revisit 알고리즘 구현.
    - [x] 퀴즈 노출 시 및 정답 시 빈도 업데이트 로직 연동 완료.
- **Priority**: High

---

### ID-22: "서책 준비 중" 인트로 화면 (Cultural Loading)
- **Completed Date**: 2026-03-10
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `CultureLoadingScreen` 구현 및 `App.kt`의 DB 동기화 섹션에 연동.
- **Priority**: Medium

---

### ID-26: Quiz Counter UX Improvement & Bug Fix
- **Description**: 퀴즈 카운트가 문제를 풀었을 때(사후)가 아닌, 새로운 문제를 불러올 때(사전) 증가하도록 로직을 변경하여 UX 불일치를 해결. 또한 ViewModel 수준에서 중복 클릭 및 Race Condition 방지 로직을 강화함.
- **Completed Date**: 2026-03-11
- **Build Verified**: [x] `./cmp/gradlew :composeApp:assembleDebug` SUCCESS
- **Results**:
    - [x] `QuizViewModel.loadNextQuiz`에서 문제 로드 시 `quizCount`를 증가시키도록 변경 (시작 시 `1 / 5` 표시).
    - [x] `QuizViewModel.checkAnswer`에서 `selectedOption != null` 체크를 추가하여 중복 이벤트 차단.
    - [x] `_state.update` 내에서 원자적 상태 업데이트(`it` 참조)를 사용하도록 리팩토링.
- **Priority**: High

---

### ID-24: Premium App Icon Design (The Calm Ink)
- **Completed Date**: 2026-03-09
- **Priority**: High

---

### ID-09: Result Screen Enhancement
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-08: Multi-Type Mixed Quiz System Development
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-06: Main Quiz Screen Implementation
- **Completed Date**: 2026-03-08
- **Priority**: Medium

---

### ID-07: Advanced Design System Construction (The Calm Ink)
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-04: Quiz Engine Algorithm
- **Completed Date**: 2026-03-08
- **Priority**: Medium

---

### ID-05: Minimalist UI Base Components
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-03: Domain Model & Data Parsing
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-01: Android Project Scaffolding
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-02: Create Idiom Data Set (JSON)
- **Completed Date**: 2026-03-07
- **Priority**: Medium
