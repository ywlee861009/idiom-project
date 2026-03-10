# 🎯 사자성어 퀴즈 안드로이드 프로젝트 요구사항 (v2.1)

## 1. 프로젝트 목표 (Project Goal)
40대 이상 사용자를 위한 **"스마트폰 속 나만의 서당"**. 서버 없이 기기 내에서 완결되는 고품격 사자성어 학습 경험 제공.

## 2. 핵심 디자인 컨셉 (Design Concept)
- **The Calm Ink**: 한지 배경(#F9F7F2) + 먹색 타이포그래피(#2C2C2C).
- **Silver-Friendly**: 최소 폰트 20sp, 주요 버튼 24sp 이상. 넓은 여백과 터치 영역.
- **Modern Floating**: 캡슐형 하단 Floating Tab Bar 적용.

## 3. 기능 요구사항 (Functional Requirements)

### 3.1 퀴즈 엔진 (Quiz Engine)
- 5문제 세션: 3종 문제(객관식, 빈칸, 순서) 혼합 출제.
- 정답 시 먹물 번짐 효과(Ink-spread) 애니메이션.

### 3.2 로컬 저장소 및 수익 모델 (Native & Monetization)
- **Only Native**: 백엔드 서버 없이 Room DB를 통한 로컬 데이터 관리.
- **Theme Purchase (IAP)**: 
    - 기본 테마(무료) 외 확장 테마 팩당 **2,000원** 판매.
    - 구글 인앱 결제(Billing API) 연동.
    - 구매 시 로컬 DB의 잠금 해제(Unlock) 처리.

### 3.3 수집 및 보상 (Collection)
- 퀴즈 완료 시 '오늘의 성어 카드' 하사.
- '서고' 메뉴에서 수집한 카드 열람 및 상세 풀이 제공.

## 4. 기술 스택 (Technical Stack)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Modern M3)
- **Data**: Room DB (Local Persistence), DataStore (Settings)
- **Payment**: Google Play Billing Library
- **Architecture**: MVI 또는 MVVM (Clean Architecture 지향)

## 5. 비즈니스 원칙
- **Privacy First**: 어떠한 개인정보도 서버로 전송하지 않음.
- **Simple Flow**: 로그인 없이 즉시 사용 가능한 구조 유지.
