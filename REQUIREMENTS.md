# 🎯 사자성어 퀴즈 안드로이드 프로젝트 요구사항 (v3.0 - 배포 사양)

## 1. 프로젝트 목표 (Project Goal)
40대 이상 사용자를 위한 **"스마트폰 속 나만의 서당"**. 서버 없이 기기 내에서 완결되는 고품격 사자성어 학습 경험 제공 및 수익화(AdMob + IAP) 구현.

## 2. 핵심 디자인 컨셉 (Design Concept)
- **The Calm Ink**: 한지 배경(#F9F7F2) + 먹색 타이포그래피(#2C2C2C).
- **Silver-Friendly**: 최소 폰트 20sp, 주요 버튼 24sp 이상. 넓은 여백과 터치 영역.
- **Modern Floating**: 캡슐형 하단 Floating Tab Bar 적용.

## 3. 기능 요구사항 (Functional Requirements)

### 3.1 퀴즈 엔진 (Quiz Engine)
- **세션 구성**: 5문제 세션, 3종 문제(객관식, 빈칸, 순서) 혼합 출제.
- **지능적 출제**: `exposureCount` 기반 미노출 성어 우선 출제 알고리즘.
- **시각 효과**: 정답 시 먹물 번짐(Ink-spread) Lottie 애니메이션.
- **콤보 시스템**: 2개 이상 연속 정답 시 콤보 활성화. 보너스 XP = `(comboCount - 1)`, 최대 **+5 XP** 캡. 오답 시 초기화. 전용 애니메이션 노출.

### 3.2 로컬 저장소 및 수익 모델 (Native & Monetization)
- **Data Persistence**: Realm Kotlin 3.0.0을 통한 로컬 데이터 관리 및 `idioms.json` 자동 동기화.
- **Google AdMob**: 전면 광고(Interstitial) 및 보상형 광고(Rewarded) 연동 (수익화 기초).
- **Theme Purchase (IAP)**: 
    - 기본 테마(무료) 외 확장 테마 팩당 **2,000원** 판매.
    - 구글 인앱 결제(Billing API) 연동 및 구매 시 로컬 DB의 잠금 해제(Unlock) 처리.

### 3.3 수집 및 보상 (Collection)
- **서고 (Collection)**: 퀴즈 완료 시 해당 문제의 성어들을 '서고'에 수집.
- **서고 상세 바텀시트**: 수집한 성어 터치 시 한자, 독음, 뜻풀이, 난이도, 네이버 사전 링크를 바텀시트로 제공.
- **학습 상세**: 서고 메뉴에서 수집한 성어의 상세 풀이 및 음성(TTS) 제공. (TTS 준비 중)

### 3.4 프로필 학습 분석 (Profile Analytics)
- **주간 학습 통계**: Canvas 기반 막대 차트로 주간 풀이 수, 정답률, 활동일 시각화.
- **월간 학습 캘린더**: 스트릭 히트맵으로 월별 학습 활동 표시. 월 이동 네비게이션 지원.
- **일일 학습 기록**: `DailyRecordEntity`를 통한 날짜별 풀이 수, 정답 수, 획득 XP 추적.

## 4. 기술 스택 (Technical Stack)
- **Language**: Kotlin (2.0.21, K2 Compiler)
- **UI Framework**: Compose Multiplatform (CMP) 1.7.3
- **Dependency Injection**: Koin 4.0.0
- **Data**: Realm Kotlin 3.0.0 (Local Persistence), DataStore 1.1.1 (Settings)
- **Infrastructure**: Firebase 33.9.0 (Crashlytics & Analytics), Google AdMob 23.6.0
- **Payment**: Google Play Billing Library (준비 중)
- **Architecture**: MVI (Clean Architecture 지향, 멀티모듈)
- **Testing**: kotlin-test, Turbine 1.2.0, Coroutines Test
- **CI/Quality**: pre-push hook (빌드 + 테스트 검증)

## 5. 배포 원칙
- **Privacy First**: 개인정보 서버 전송 없음.
- **Simple Flow**: 로그인 없이 즉시 사용 가능한 구조.
- **Quality First**: 배포 전 모든 화면에서 `20sp` 이상 가독성 검증 필수.
