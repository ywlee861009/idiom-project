# 📋 To Do Tickets (백로그)

노인분들을 위한 고가용성 및 지능적 학습 시스템 구축 로드맵입니다.

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

