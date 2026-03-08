# 🤖 Android Senior Developer Continuity Guide

이 문서는 **사자성어 퀴즈 안드로이드 프로젝트**의 설계 철학, 기술 스택, 멀티모듈 구조 및 개발 환경 설정을 설명합니다. 새로운 개발 환경에서 작업을 시작할 때 이 가이드를 반드시 숙지하십시오.

---

## 1. 기술 스택 (Technical Stack)
- **Language**: Kotlin 1.9.22
- **UI Framework**: Jetpack Compose (Modern Toolkit)
- **Architecture**: MVI (Model-View-Intent) + Clean Architecture
- **Navigation**: Navigation 3 (Type-safe Navigation with Kotlin Serialization)
- **Dependency Injection**: Hilt (Dagger-Hilt)
- **Asynchronous**: Kotlin Coroutines & Flow
- **Build Tool**: Gradle (Kotlin DSL) with Version Catalog (`libs.versions.toml`)

## 2. 프로젝트 아키텍처 (Architecture)

### 멀티모듈 구조 (Multi-module Strategy)
프로젝트는 관심사 분리와 빌드 최적화를 위해 다음과 같은 레이어별 모듈로 구성됩니다.

- **`:app`**: 앱의 진입점. Hilt Application 설정 및 `Navigation 3`의 전역 NavHost 관리.
- **`:core`**: 공통 디자인 시스템(Theme, Color), 공통 UI 컴포넌트, 네비게이션 목적지(`Destination`) 정의.
- **`:domain`**: 비즈니스 로직의 중심. `UseCase`, 도메인 모델, `Repository` 인터페이스 정의. (프레임워크 비의존적)
- **`:data`**: 데이터 소스 관리. `Repository` 구현체, JSON 파싱 로직, Local Storage 관리.
- **`:feature:quiz`**: 퀴즈 핵심 기능을 담당하는 피처 모듈. (MVI 패턴 적용)
- **`:feature:result`**: 퀴즈 결과 리포트를 담당하는 피처 모듈. (MVI 패턴 적용)

### MVI 패턴 흐름
각 피처 모듈은 `Contract`를 통해 상태를 관리합니다.
1.  **State**: UI를 나타내는 단일 불변(Immutable) 객체.
2.  **Intent**: 사용자의 액션이나 시스템 이벤트 정의.
3.  **Side Effect**: 화면 이동, 토스트 등 일회성 이벤트 처리.

## 3. 환경 설정 및 실행 방법 (Setup & Run)

1.  **Android Studio**: 최신 버전(Hedgehog 이상 권장)을 설치합니다.
2.  **프로젝트 열기**: `android/` 폴더를 루트로 하여 프로젝트를 엽니다. (루트 디렉토리가 아님에 유의)
3.  **Gradle Sync**: `libs.versions.toml`을 기반으로 의존성을 동기화합니다.
4.  **빌드 및 실행**: `app` 모듈을 선택하고 기기 또는 에뮬레이터에서 실행합니다.

## 4. 개발 가이드라인 (Development Rules)

- **Type-safe Navigation**: 모든 화면 이동은 `core` 모듈에 정의된 `Destination` 클래스를 통해 타입 세이프하게 수행합니다.
- **Unidirectional Data Flow**: UI 상태 변경은 반드시 ViewModel을 통한 `State` 업데이트로만 이루어져야 합니다.
- **Material 3**: 디자인 컨셉인 'Extreme Minimalism'을 유지하기 위해 M3 컴포넌트와 `core` 모듈의 테마 설정을 준수합니다.
- **Modularization**: 새로운 기능을 추가할 때는 `:feature:xxx` 모듈을 생성하여 독립성을 유지합니다.

## 5. 주요 파일 위치
- **의존성 관리**: `android/gradle/libs.versions.toml`
- **전역 테마**: `android/core/src/main/java/com/kero/idiom/core/theme/`
- **사자성어 데이터**: `android/data/src/main/assets/idioms.json`

---
*Last Updated: 2026-03-08*
*Author: Senior Android Developer Kero (Gemini CLI)*
