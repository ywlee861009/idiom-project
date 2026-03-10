# 🧭 Idiom Quiz UI Flow & Navigation (v2.0)

본 문서는 40대 이상 사용자를 위한 **극강의 심플함**과 **Floating Tab** 기반의 확장성을 목표로 설계되었습니다.

## 1. 네비게이션 원칙 (Navigation Principles)
- **Extreme Minimalism**: 화면 하단 중앙에 떠 있는 캡슐형 Floating Tab Bar 사용.
- **Zero Learning Curve**: 모든 메뉴는 1-Depth로 접근 가능.
- **Tactile UX**: 버튼 크기를 극대화하여 오터치 방지.

## 2. 전체 화면 구조 (App Map)

### [A] Entry Layer
- **Splash Screen**: 붓글씨 애니메이션 + '사자성어' 타이틀 (1.5초 이내).
- **Home (서당)**: 
    - 중앙에 커다란 "오늘의 강독 시작" 버튼.
    - 현재 나의 칭호(예: 초립동이)와 오늘 푼 문제 수 노출.

### [B] Main Navigation (Floating Tab)
1.  **서당 (Home)**: 퀴즈 진입 및 일일 학습 현황.
2.  **서고 (Collection)**: 
    - 내가 획득한 성어 카드 리스트.
    - **[유료]** 잠겨있는 테마 팩(2,000원) 리스트 노출 및 구매 유도.
3.  **내 정보 (Profile)**: 
    - 학습 통계, 알람 설정, 폰트 크기 조절.

### [C] Experience Layer (Interactive)
- **Quiz Session**: 
    - 3종 혼합 문제 (뜻 보고 맞히기, 빈칸 채우기, 순서 맞히기).
    - 퀴즈 도중에는 Floating Tab을 숨겨 몰입도 극대화.
- **Reward Overlay**: 
    - 5문제 완료 시 '서첩'이 펼쳐지며 오늘의 카드 하사.

## 3. 사용자 흐름 (User Flow)

1.  **앱 실행** -> Splash -> **[서당(Home)]**
2.  **[서당]** -> "강독 시작" 터치 -> **[퀴즈]** (5문제)
3.  **퀴즈 완료** -> **[보상 팝업]** -> 카드 확인 후 **[서당]** 복귀.
4.  **Floating Tab** 터치 -> **[서고]** 이동 -> 유료 테마 구매 또는 복습.

---
*Last Updated: 2026-03-10*  
*Approved by: CEO*
