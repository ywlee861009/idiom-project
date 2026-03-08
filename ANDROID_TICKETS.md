# 🎫 Android Development Tickets (Kero's Board)

이 문서는 안드로이드 시니어 개발자 **케로**가 수행해야 할 상세 구현 티켓들을 관리합니다. 각 티켓은 MVI 아키텍처와 멀티모듈 구조를 준수하여 설계되었습니다.

---

## 🟢 [Done] 완료된 작업

### AID-01: Multi-module Scaffolding & Modern Stack Setup
- **설명**: 프로젝트를 6개의 멀티모듈 구조로 재편하고 최신 기술 스택(MVI, Compose, Navigation 3)을 설정함.
- **수행 내용**:
    - [x] `:app`, `:core`, `:domain`, `:data`, `:feature:quiz`, `:feature:result` 모듈 생성 및 의존성 설정.
    - [x] `libs.versions.toml`을 통한 중앙 집중식 버전 관리 (Navigation 2.8.7 적용).
    - [x] 모든 모듈 `jvmToolchain(17)` 적용하여 빌드 안정성 확보.
    - [x] `Destination.kt`를 통한 타입 세이프 네비게이션 뼈대 구축.
    - [x] `android/.gitignore` 표준화 및 Git 인덱스 정리.

---

## 🟡 [To Do] 우선순위 작업

### AID-02: Data Layer Implementation (JSON Parsing)
- **설명**: `assets/idioms.json` 데이터를 도메인 모델로 변환하고 공급하는 저장소 레이어 구현.
- **요구사항**:
    - [ ] `android/data/src/main/assets/` 폴더로 사자성어 데이터 이동.
    - [ ] `kotlinx-serialization`을 이용한 JSON 파싱 로직 작성.
    - [ ] `IdiomRepository` 인터페이스(domain) 및 구현체(data) 작성.
    - [ ] Hilt를 이용한 `Repository` 의존성 주입 설정.
- **우선순위**: High

### AID-03: Quiz Feature MVI Implementation
- **설명**: `:feature:quiz` 모듈의 MVI 로직과 퀴즈 화면 UI 구현.
- **요구사항**:
    - [ ] `QuizViewModel` 작성 (State, Intent, SideEffect 처리).
    - [ ] `domain` 레이어의 `GetRandomIdiomUseCase` 연동.
    - [ ] Compose 기반 퀴즈 화면 UI (미니멀리즘 디자인) 구현.
    - [ ] 정답 체크 및 점수 계산 로직 포함.
- **우선순위**: High

### AID-04: Result Feature MVI Implementation
- **설명**: `:feature:result` 모듈의 결과 화면 및 재시작 로직 구현.
- **요구사항**:
    - [ ] 퀴즈 결과(점수) 수신 및 표시.
    - [ ] "다시 풀기" 버튼 클릭 시 퀴즈 화면으로 네비게이션 처리.
- **우선순위**: Medium

---

## 🔵 [Backlog] 추후 작업

### AID-05: Local Storage Integration (DataStore)
- **설명**: Jetpack DataStore를 사용하여 최고 점수(High Score)를 로컬에 저장.

### AID-06: UI Polish & Animation
- **설명**: 정답/오답 시 컬러 전환 애니메이션 및 결과 화면 통계 연출 강화.

---
*Last Updated: 2026-03-08*
*Assigned to: Senior Android Developer Kero*
