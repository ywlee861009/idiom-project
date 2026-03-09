# 🧠 사자성어 퀴즈 (Idiom Quiz) - Android Premium

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose)
![Architecture](https://img.shields.io/badge/Architecture-Clean_MVI-FF4081?style=flat-square)
![Design](https://img.shields.io/badge/Design-Extreme_Minimalism-lightgrey?style=flat-square)

**사자성어 퀴즈**는 어르신들을 위한 고가용성 학습 경험(Silver UX)과 현대적인 미니멀리즘 디자인을 결합한 프리미엄 안드로이드 애플리케이션입니다. '온고지신'의 정신으로 고전의 지혜를 가장 세련된 방식으로 전달합니다.

---

## ✨ 핵심 가치 (Core Values)
- **Silver UX & Accessibility**: 어르신들을 위한 대형 폰트(64sp+), 명조체 테마, 직관적인 레이아웃.
- **Extreme Minimalism**: 불필요한 장식을 배제하고 타이포그래피와 여백의 미를 극대화.
- **Intelligent Learning**: 사용자의 학습 이력을 분석하여 취약한 사자성어를 우선 노출하는 스마트 퀴즈 엔진.
- **Cultural Aesthetics**: '한지' 질감의 배경과 '먹물 번짐' 애니메이션을 통한 동양적 감성 구현.

## 🛠 기술 스택 (Modern Tech Stack)
- **Language**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: Clean Architecture + MVI (Model-View-Intent)
- **Dependency Injection**: Hilt
- **Concurrency**: Coroutines & Flow
- **Data Persistence**: Room (Smart JSON Migration)
- **Build System**: Gradle Kotlin DSL + Multi-Module Strategy

## 📂 멀티 모듈 구조 (Multi-Module Architecture)
프로젝트는 관심사 분리와 유지보수성을 위해 다음과 같이 구조화되어 있습니다.
- `:app`: 의존성 주입 설정 및 네비게이션 메인 엔트리.
- `:core`: 공통 UI 컴포넌트(HanjiBackground, IdiomButton), 테마, 유틸리티.
- `:data`: Room DB, API/JSON 데이터 소스 및 리포지토리 구현체.
- `:domain`: 순수 비즈니스 로직, UseCase 및 도메인 모델.
- `:feature:quiz`: 퀴즈 풀이 화면 및 MVI 로직.
- `:feature:result`: 퀴즈 결과 리포트 및 통계 화면.

## 📋 프로젝트 관리 (Project Management)
본 프로젝트는 AI 페르소나 시스템을 통해 체계적으로 관리됩니다.
- **PM Mark**: 프로젝트 기획, 일정 관리, `REQUIREMENTS.md`, `ROADMAP.md` 담당.
- **Dev Kero**: 시니어 안드로이드 개발자. 아키텍처 및 핵심 로직 구현 담당.
- **Designer Jenny**: 시니어 프로덕트 디자이너. UI/UX 및 디자인 시스템 담당.

### 관련 문서 (Documentation)
- [🗺️ 프로젝트 상태 (PROJECT_STATE.md)](./PROJECT_STATE.md): 현재 개발 단계 및 마일스톤.
- [📝 요구사항 (REQUIREMENTS.md)](./REQUIREMENTS.md): 상세 기능 및 디자인 가이드.
- [🛣️ 로드맵 (ROADMAP.md)](./ROADMAP.md): 전체 개발 일정.
- [🎫 티켓 관리 (tickets/)](./tickets/): 상태별 상세 작업 티켓 (`todo.md`, `in-progress.md`, `done.md`).

---

## 🏗 시작하기 (Getting Started)
1. Android Studio (Ladybug 2024.2.1 이상)를 설치합니다.
2. 프로젝트를 클론하고 `gradlew build`를 실행합니다.
3. 기기 또는 에뮬레이터에서 `app` 모듈을 실행합니다.

---
**Author**: Mark (PM), Kero (Dev), Jenny (Designer)  
**Last Updated**: 2026-03-09
