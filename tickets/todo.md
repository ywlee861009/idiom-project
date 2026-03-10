# 📋 To Do Tickets (백로그)

노인분들을 위한 고가용성 및 지능적 학습 시스템 구축 로드맵입니다.

---

### ID-25: 상세 UI 디자인 - Floating Tab & 3단 구조 (Jenny)
- **Assignee**: Designer (Jenny)
- **Description**: 사장님의 지시에 따른 차세대 UI 구조(서당-서고-내정보)와 캡슐형 Floating Tab Bar의 고해상도 디자인 가이드 제작. 40대 이상 사용자를 고려한 'The Calm Ink' 디자인 시스템의 확장판.
- **Acceptance Criteria**:
    - [ ] **Floating Tab Bar**: 화면 하단에 반투명 캡슐 형태로 떠 있는 세련된 탭 바 디자인.
    - [ ] **서당 (Home)**: "강독 시작" 버튼이 강조된 메인 대시보드 레이아웃.
    - [ ] **서고 (Collection)**: 유료 테마(2,000원)와 무료 테마가 구분된 카드 리스트 UI.
    - [ ] **내 정보 (Profile)**: 큼직한 텍스트와 직관적인 설정 항목 UI.
    - [ ] **Silver UX**: 최소 폰트 20sp 이상, 주요 버튼 24sp 이상의 가독성 확보.
- **Technical Notes**:
    - `UI_FLOW.md` v2.0 기반으로 설계할 것.
    - 기존 'The Calm Ink' 팔레트(#F9F7F2, #2C2C2C) 엄수.
- **Priority**: Highest

---

### ID-20: Room DB 및 초기 동기화 (Smart Migration)
- **Assignee**: Android Developer (Kero)
- **Description**: `assets/idioms.json`의 데이터를 Room DB로 마이그레이션. JSON 버전을 체크하여 최초 실행 혹은 업데이트 시에만 실행. `exposureCount`(노출 빈도) 필드 필수 포함.
- **Priority**: Highest

---

### ID-21: 지능적 퀴즈 추출 알고리즘 (Frequency-Based Random)
- **Assignee**: Android Developer (Kero)
- **Description**: `exposureCount`가 낮은 사자성어가 더 높은 확률로 출제되도록 리포지토리 로직 수정. 문제 출제 시마다 해당 사자성어의 `exposureCount`를 1씩 증가.
- **Priority**: High

---

### ID-22: "서책 준비 중" 인트로 화면 (Cultural Loading)
- **Assignee**: Android Developer (Kero)
- **Description**: 초기 DB 동기화 동안 보여줄 로딩 화면. 한지에 먹물이 서서히 번지거나 서책이 펼쳐지는 듯한 동양적 미학 적용.
- **Priority**: Medium

---

### ID-23: Global Typography Upscale (Silver UX)
- **Assignee**: Android Developer (Kero)
- **Description**: 어르신들을 위한 64sp급 대형 폰트 및 명조체(Serif) 테마 전면 적용. 모든 UI 컴포넌트의 여백과 높이를 커진 폰트에 맞춰 재조정.
- **Priority**: High
