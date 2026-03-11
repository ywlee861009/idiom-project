# 📋 To Do Tickets (백로그)

노인분들을 위한 고가용성 및 지능적 학습 시스템 구축 로드맵입니다.

---

### ID-23: Global Typography Upscale (Silver UX)
- **Assignee**: Android Developer (Kero)
- **Description**: 어르신들을 위한 64sp급 대형 폰트 및 명조체(Serif) 테마 전면 적용. `Type.kt`의 본문 텍스트(bodyLarge 등)를 최소 20sp 이상으로 확대하고, 모든 UI 컴포넌트의 여백을 재조정.
- **Status**: In Progress (Partial migration done)
- **Priority**: High

---

### ID-29: [Level 2] 주관식 2칸 채우기 퀴즈 개발
- **Assignee**: Android Developer (Kero)
- **Description**: 
    - 사자성어 중 2개 칸을 비우고, 사용자가 직접 한글로 입력하여 맞히는 유형.
    - 객관식 보기가 없으므로 난이도가 상승함.
- **Acceptance Criteria**:
    - [ ] 퀴즈 엔진에서 2개의 랜덤 위치에 빈칸 생성 로직 추가.
    - [ ] 한글 입력 전용 대형 텍스트 필드 UI 구현.
    - [ ] 입력값 실시간 검증 및 정답 처리 로직 연동.
- **Priority**: High

---

### ID-30: [Level 3] 주관식 전체 채우기 퀴즈 개발
- **Assignee**: Android Developer (Kero)
- **Description**: 
    - 뜻이나 한자만 보여주고 4글자 전체를 한글로 입력하여 맞히는 최고 난이도 유형.
- **Acceptance Criteria**:
    - [ ] 4글자 전체 입력용 UI 레이아웃 구현.
    - [ ] 정답 확인 시 4글자 완전 일치 여부 판단.
- **Priority**: Medium

---

### ID-27: IAP Module Integration (Phase 8 Preparation)
- **Assignee**: Android Developer (Kero)
- **Description**: 구글 플레이 인앱 결제(Billing API) 라이브러리 연동 및 테마 잠금 해제 인터페이스 설계.
- **Priority**: Medium

---

### ID-28: Theme Pack UI & Unlock Logic
- **Assignee**: Android Developer (Kero)
- **Description**: '서고' 화면에서 유료 테마(2,000원) 카드 리스트 표시 및 구매 완료 시 DB `isLocked` 필드 업데이트 로직 구현.
- **Priority**: Medium
