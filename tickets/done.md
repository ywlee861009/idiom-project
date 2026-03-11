# ✅ Done Tickets (히스토리)

이 문서는 구현 및 검증이 완료된 티켓들의 기록입니다.

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
- **Description**: 'The Calm Ink' 디자인 시스템을 응축한 앱 아이콘 자산 제작. 배경(Hanji #F9F7F2)과 전경(Minimalist Symbol #2C2C2C) 레이어를 분리하여 제작 완료.
- **Completed Date**: 2026-03-09
- **Deliverables**:
    - [x] `design/icon_background.svg`: 한지 질감의 배경 레이어.
    - [x] `design/icon_foreground.svg`: 서책과 먹물을 형상화한 전경 심볼 레이어.
- **Priority**: High

---

### ID-09: Result Screen Enhancement
- **Description**: 'The Calm Ink' 디자인 시스템을 적용하여 결과 화면을 구현. 퀴즈 종료 후 최종 점수와 '다시 하기', '홈으로' 버튼을 포함.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew assembleDebug` SUCCESS
- **Results**:
    - [x] `ResultContract`, `ResultViewModel` (MVI) 구현 완료.
    - [x] 'The Calm Ink' 팔레트 및 `IdiomBaseCard`, `IdiomPrimaryButton` 적용.
    - [x] 퀴즈 종료 시 자동 이동 및 다시 하기/홈 이동 네비게이션 연동 완료.
- **Priority**: High

---

### ID-08: Multi-Type Mixed Quiz System Development
- **Description**: 3가지 유형(빈칸 채우기, 뜻 맞히기, 한자 읽기)의 퀴즈 혼합 시스템 구축.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew assembleDebug` SUCCESS
- **Results**:
    - [x] `QuizType` (A, B, C) 도메인 모델 설계 완료.
    - [x] 무작위 유형 선택 및 유형별 데이터 생성 로직 구현.
    - [x] 퀴즈 유형별 동적 UI 레이아웃(텍스트 크기, 힌트 등) 대응 완료.
- **Priority**: High

---

### ID-06: Main Quiz Screen Implementation
- **Description**: ID-04(알고리즘), ID-07(디자인) 결과물을 결합한 실제 퀴즈 화면 구현.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew assembleDebug` SUCCESS
- **Priority**: Medium

---

### ID-07: Advanced Design System Construction (The Calm Ink)
- **Description**: 'The Calm Ink' 컨셉의 컬러 팔레트를 기반으로 디자인 시스템 고도화.
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :core:assembleDebug` SUCCESS
- **Priority**: High

---

### ID-04: Quiz Engine Algorithm
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :domain:assembleDebug` SUCCESS
- **Priority**: Medium

---

### ID-05: Minimalist UI Base Components
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :core:assembleDebug` SUCCESS
- **Priority**: High

---

### ID-03: Domain Model & Data Parsing
- **Completed Date**: 2026-03-08
- **Build Verified**: [x] `./gradlew :data:assembleDebug` SUCCESS
- **Priority**: High

---

### ID-01: Android Project Scaffolding
- **Completed Date**: 2026-03-08
- **Priority**: High

---

### ID-02: Create Idiom Data Set (JSON)
- **Completed Date**: 2026-03-07
- **Priority**: Medium
