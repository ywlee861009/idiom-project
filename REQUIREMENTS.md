# 사자성어 퀴즈 안드로이드 프로젝트 요구사항 (REQUIREMENTS)

## 1. 프로젝트 목표 (Project Goal)
사용자가 심플하고 직관적인 UI를 통해 사자성어 퀴즈를 풀고 실력을 쌓을 수 있는 안드로이드 앱 개발.

## 2. 디자인 컨셉 (Design Concept)
- **Extreme Minimalism**: 불필요한 아이콘이나 화려한 배경 배제.
- **Typography Focused**: 깨끗한 폰트와 충분한 여백(Whitespace) 강조.
- **Material 3 (M3)**: 구글의 최신 디자인 시스템 컴포넌트 활용.
- **Color Palette**: 톤 다운된 차분한 색상 (예: Light Grey, Deep Navy, Soft White).

## 3. 핵심 기능 (Core Features - MVP)
- [ ] **퀴즈 엔진 (Quiz Engine)**:
    - 사자성어 데이터(`assets/idioms.json`) 로드.
    - 무작위 문제 생성 (사자성어 중 한 글자 비우기).
    - 4개의 객관식 보기 제공 또는 직접 입력 (사용자 선택 가능하게 설계).
- [ ] **점수 시스템 (Scoring)**:
    - 정답 시 점수 획득, 실시간 점수 상단 표시.
- [ ] **로컬 저장소 (Local Storage)**:
    - 최고 점수(High Score) 저장.
    - 진행 상황 저장.
- [ ] **결과 화면 (Result Screen)**:
    - 퀴즈 종료 후 최종 점수 및 통계 표시.

## 4. 기술 스택 (Technical Stack)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Modern Toolkit)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Data Storage**: DataStore (Simple Key-Value) or Room (SQLite)
- **Build Tool**: Gradle (Kotlin DSL)
- **Min SDK**: 26 (Android 8.0 이상)

## 5. 미결정/추후 고려 사항 (Backlog)
- [ ] 다크 모드(Dark Mode) 지원.
- [ ] 사자성어 즐겨찾기(Favorite) 기능.
- [ ] 정답 확인 시 상세 한자 풀이 팝업.
