# 🧠 사자성어 퀴즈 (Idiom Quiz) - Android MVP

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose)
![Design](https://img.shields.io/badge/Design-Minimalism-lightgrey?style=flat-square)

심플하고 직관적인 디자인을 통해 사자성어를 학습하고 퀴즈를 풀 수 있는 안드로이드 애플리케이션입니다. **Extreme Minimalism** 컨셉을 바탕으로 사용자에게 본질적인 학습 경험을 제공합니다.

---

## 🚀 핵심 기능 (Core Features)
- **미니멀리즘 UI**: Material 3 기반의 깨끗하고 정돈된 퀴즈 화면.
- **200+ 데이터셋**: 엄선된 200개의 사자성어 데이터 내장 (`assets/idioms.json`).
- **퀴즈 엔진**: 랜덤 사자성어 문제 생성 및 실시간 점수 시스템.
- **로컬 저장소**: 최고 점수 및 학습 진행 상황 자동 저장.

## 🛠 기술 스택 (Tech Stack)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Data Persistence**: DataStore / Room
- **Build System**: Gradle (Kotlin DSL)

## 📂 프로젝트 구조 (Architecture)
본 프로젝트는 확장성을 고려하여 레이어드 아키텍처를 따릅니다.
- `data/`: JSON 데이터 파싱 및 로컬 저장소 로직.
- `domain/`: 사자성어 모델 및 비즈니스 로직(Use Case).
- `ui/`: Compose 기반의 스크린 및 ViewModel.
- `assets/`: 사자성어 JSON 데이터셋.

## 📋 프로젝트 관리 (Project Management)
이 프로젝트는 AI PM 지침에 따라 체계적으로 관리됩니다. 상세 내용은 아래 문서를 참고하세요.
- [PM 가이드 (GUIDE_FOR_PM.md)](./GUIDE_FOR_PM.md): 프로젝트 복구 및 지속 가이드.
- [요구사항 (REQUIREMENTS.md)](./REQUIREMENTS.md): 상세 기능 및 디자인 명세.
- [로드맵 (ROADMAP.md)](./ROADMAP.md): 전체 개발 일정 및 마일스톤.
- [티켓 (TICKETS.md)](./TICKETS.md): 현재 진행 중인 개발 태스크 목록.

---

## 🏗 시작하기 (Getting Started)
1. Android Studio (Ladybug 이상 권장)를 설치합니다.
2. 본 레포지토리를 클론합니다.
3. `com.kero.idiom` 패키지 구조로 빌드 및 실행합니다.

---
**Author**: Project Manager (Gemini CLI)  
**Last Updated**: 2026-03-07
