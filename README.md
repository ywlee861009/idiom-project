# 🌌 Idiom Quiz: 스마트폰 속 나만의 서당 (The Calm Ink)

> **"어르신들의 두뇌 건강을 위한, 가장 고요하고 우아한 사자성어 학습 앱"**

본 프로젝트는 40대 이상 사용자를 타겟으로 한 **Extreme Minimalism** 디자인 철학 기반의 퀴즈 애플리케이션입니다. **Compose Multiplatform (CMP)**을 기반으로 설계되었으며, 현대적인 안드로이드 및 멀티플랫폼 기술 스택의 정수를 담고 있습니다.

---

## 🛠️ 기술 스택 상세 명세 (Full Technical Stack)

### 1. 핵심 언어 및 프레임워크 (Core Stack)
- **Kotlin 2.1.10**: 최신 Kotlin K2 컴파일러를 사용하여 빌드 속도와 런타임 성능을 극대화했습니다.
- **Compose Multiplatform (CMP) 1.7.3**: 선언형 UI 프레임워크를 통해 Android와 차후 확장될 iOS 플랫폼 간의 UI 로직 100% 공유를 실현했습니다.
- **Jetpack Compose Compiler 2.1.10**: 최신 컴파일러를 통한 강력한 상태 관리와 UI 최적화를 적용했습니다.

### 2. 아키텍처 및 상태 관리 (Architecture)
- **MVI (Model-View-Intent)**: 단방향 데이터 흐름(UDF) 아키텍처를 채택하여 복잡한 퀴즈 상태를 예측 가능하게 관리합니다.
- **Koin 4.0.0 (Dependency Injection)**: 
    - `koin-compose`, `koin-compose-viewmodel`: CMP 환경에 최적화된 경량 의존성 주입 도구를 사용하여 모듈 간 결합도를 낮췄습니다.
- **Coroutines 1.9.0 & Flow**: 비동기 스트림 처리를 통해 매끄러운 사용자 경험을 제공합니다.
- **Kotlinx Serialization 1.7.3**: JSON 데이터(idioms.json)를 고속으로 파싱하고 타입 안정성을 보장합니다.

### 3. 데이터 및 영속성 (Data Storage)
- **Room DB 2.7.0-alpha11 (KMP)**: 최신 Room KMP 버전을 사용하여 로컬 데이터베이스를 관리하며, SQLite Bundled 버전을 통해 일관된 DB 성능을 보장합니다.
- **DataStore Preferences 1.1.1**: 유저의 설정값(알림 설정, 동기화 버전 등)을 안전하게 저장합니다.
- **Repository Pattern**: `AssetIdiomDataSource`와 `IdiomDao`를 결합하여 단일 데이터 소스 원칙(Single Source of Truth)을 준수합니다.

### 4. 시각 및 애니메이션 (UI/UX)
- **Material 3 (M3)**: 최신 디자인 가이드를 준수하면서도 'The Calm Ink'만의 미니멀리즘을 구현했습니다.
- **Compottie 2.0.0-rc02 (Lottie for CMP)**: 
    - `success.lottie`, `wrong.lottie`: 먹물 번짐 효과와 같은 동양적 애니메이션을 고해상도 벡터로 구현했습니다.
- **Jetpack Compose Navigation (CMP Compatible)**: Type-safe Navigation을 통해 화면 간 이동의 안정성을 확보했습니다.

### 5. 인프라 및 분석 (Infra & Analytics)
- **Firebase 33.9.0 (BOM)**:
    - **Analytics**: 사용자 학습 패턴 및 체류 시간 분석.
    - **Crashlytics**: 실시간 앱 오류 트래킹 및 안정성 확보.
- **Google AdMob 23.6.0**: 보상형 광고(Rewarded Ads) 시스템을 위한 기반 설계를 완료했습니다.
- **WorkManager 2.10.0**: 유저의 재방문을 유도하는 지능적 로컬 알림(Reminder) 시스템을 백그라운드에서 제어합니다.

### 6. 빌드 시스템 (Build & Gradle)
- **Gradle 8.10.2+**: 최신 빌드 도구를 사용합니다.
- **Version Catalogs (`libs.versions.toml`)**: 모든 의존성을 중앙 관리하여 버전 충돌을 방지하고 유지보수성을 극대화했습니다.
- **KSP (Kotlin Symbol Processing)**: Room DB 및 컴파일 타임 최적화를 위해 최신 KSP를 활용합니다.

---

## 🧠 핵심 비즈니스 알고리즘

### 📊 Smart Revisit Algorithm (지능적 복습)
- 단순 무작위 추출이 아닌, `exposureCount`(노출 빈도)와 `correctCount`(정답 횟수)를 실시간 분석하여 유저가 완벽하게 암기할 때까지 지능적으로 문제를 출제합니다.

### 📜 5-Level Multi-Tier Quiz
- **Level 1**: 객관식 3종 (입문)
- **Level 2**: 주관식 2칸 채우기 (중급)
- **Level 3**: 주관식 전체 채우기 (고급)
- **Level 4-5**: 상황 매칭 및 단기 기억력 테스트 (준비 중)

---
*Last Updated: 2026-03-11*  
*Architected & Documented by PM Mark*
