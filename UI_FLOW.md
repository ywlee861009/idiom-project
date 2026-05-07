# 🧭 Idiom Quiz UI Flow & Navigation (v3.0)

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
    - 카드 터치 시 **상세 바텀시트** 표시 (한자, 독음, 뜻풀이, 난이도 별점, 네이버 사전 링크).
    - **[유료]** 잠겨있는 테마 팩(2,000원) 리스트 노출 및 구매 유도.
3.  **내 정보 (Profile)**: 
    - **주간 학습 통계**: Canvas 기반 막대 차트 (풀이 수, 정답률, 활동일).
    - **월간 학습 캘린더**: 스트릭 히트맵, 월 이동 네비게이션.
    - 알람 설정, 폰트 크기 조절.

### [C] Experience Layer (Interactive)
- **Quiz Session**: 
    - 3종 혼합 문제 (뜻 보고 맞히기, 빈칸 채우기, 순서 맞히기).
    - 퀴즈 도중에는 Floating Tab을 숨겨 몰입도 극대화.
    - **콤보 시스템**: 2연속 정답부터 콤보 UI 표시, 최대 +5 XP 보너스.
- **Reward Screen**: 
    - 5문제 완료 시 점수(score), 총 문제 수(total), 획득 XP(xpGained) 표시.
    - 오늘의 카드 하사 및 서고 수집 연동.

## 3. 사용자 흐름 (User Flow)

1.  **앱 실행** -> Splash -> **[서당(Home)]**
2.  **[서당]** -> "강독 시작" 터치 -> **[퀴즈]** (5문제)
3.  **퀴즈 완료** -> **[보상 화면]** -> 점수·XP 확인 후 **[서당]** 복귀.
4.  **Floating Tab** 터치 -> **[서고]** 이동 -> 성어 카드 터치 -> **[상세 바텀시트]** -> 복습.
5.  **Floating Tab** 터치 -> **[내 정보]** 이동 -> 주간 차트 및 월간 캘린더 확인.

---
*Last Updated: 2026-05-07*  
*Approved by: CEO*
