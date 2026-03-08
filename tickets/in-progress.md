# 🏗️ In Progress Tickets (작업 중)

현재 개발자가 수행 중인 작업 목록입니다. 한 번에 너무 많은 티켓을 올리지 않도록 관리합니다 (WIP Limit).

---

### ID-03: Domain Model & Data Parsing
- **Assignee**: Android Developer (Kero)
- **Description**: `idioms.json` 데이터를 앱 내부에서 사용할 수 있는 도메인 모델로 변환.
- **Acceptance Criteria**:
    - [ ] JSON 파일을 읽어 `List<Idiom>`으로 변환하는 `IdiomDataSource` 구현.
    - [ ] `IdiomRepository` 인터페이스 및 구현체 작성.
    - [ ] Hilt 의존성 주입 연동.
- **Status**: Implementing data layer logic...
- **Priority**: High
