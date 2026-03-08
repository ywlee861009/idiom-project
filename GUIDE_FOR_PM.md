# 🧠 PM & Developer Continuity Guide

이 문서는 이 프로젝트의 **Project Manager(PM)**와 **Developer**가 프로젝트의 맥락을 즉시 파악하고 업무를 지속하기 위한 가이드라인입니다. 새로운 환경이나 세션에서 작업을 시작할 때, PM은 반드시 이 파일을 먼저 읽고 현재 상태를 동기화해야 합니다.

---

## 1. 프로젝트 개요 (Overview)
- **프로젝트 명**: 사자성어 퀴즈 (Idiom Quiz Project)
- **핵심 목표**: 사용자가 사자성어 퀴즈를 풀고 점수를 획득하는 학습형 앱 개발.
- **현재 단계**: [아이디어 구체화 단계] (2026-03-07 기준)

## 2. 업무 수행 주체 (Roles)
- **PM (AI)**: 아이디어 구체화, `REQUIREMENTS.md` 관리, `ROADMAP.md` 일정 관리, `TICKETS.md` 발행.
- **Developer (AI)**: `TICKETS.md`에 정의된 할 일을 수행하고 코드를 구현 및 테스트.
- **User (Owner)**: 최종 의사결정 및 피드백 제공.

## 3. 작업 환경 복구 방법 (Environment Setup)
새로운 컴퓨터나 환경에서 작업을 재개할 때 다음 순서를 따릅니다.

1.  **프로젝트 체크아웃**: Git 저장소 또는 프로젝트 폴더를 복제합니다.
2.  **PM 스킬 설치**: 
    - 프로젝트 루트에 있는 `project-manager.skill` 파일을 확인합니다.
    - 아래 명령어를 실행하여 PM 전용 지침을 설치합니다.
      ```bash
      gemini skills install project-manager.skill --scope workspace
      ```
    - Gemini CLI에서 `/skills reload`를 입력하여 스킬을 활성화합니다.
3.  **컨텍스트 동기화**: 
    - PM은 `REQUIREMENTS.md`, `ROADMAP.md`, `TICKETS.md`를 차례로 읽어 현재 진행 상황을 파악합니다.

## 4. 문서 구조 및 관리 규칙 (Documentation Rules)
모든 PM 업무는 아래 파일들을 통해 관리되며, 파일 수정 시 반드시 최신 상태를 유지해야 합니다.

- **`REQUIREMENTS.md`**: 요구사항 정의서. 기능의 '범위'를 결정합니다.
- **`ROADMAP.md`**: 전체 일정표. '언제' 무엇을 할지 결정합니다.
- **`tickets/`**: 상태별 티켓 관리 디렉토리.
    - `todo.md`: 아직 시작하지 않은 백로그.
    - `in-progress.md`: 현재 진행 중인 티켓.
    - `done.md`: 완료된 티켓들의 히스토리.
- **`skills/project-manager/`**: PM의 전문 지침과 티켓 템플릿이 들어있는 스킬 폴더입니다.

## 5. 업무 프로세스 (Workflow)
1.  **아이디어 논의**: 사용자와 대화하며 아이디어를 구체화하고 `REQUIREMENTS.md`를 업데이트합니다.
2.  **일정 수립**: 요구사항이 확정되면 `ROADMAP.md`에 마일스톤을 설정합니다.
3.  **티켓 발행**: 개발 단계에 맞춰 `TICKETS.md`에 새로운 할 일을 추가합니다.
4.  **개발 및 검증**: 개발자가 코드를 작성하면 PM은 `REQUIREMENTS.md`의 체크박스를 업데이트합니다.

## 6. 현재 미결정 사항 (Pending Decisions)
- [ ] 퀴즈의 구체적인 UI/UX 방식 (한 글자 비우기 vs 전체 입력)
- [ ] 최종 기술 스택 (Python CLI vs Web/React)
- [ ] 데이터 관리 방식 (JSON 파일 vs 외부 API)

---
*Last Updated: 2026-03-07*
*Author: Project Manager Mark (Gemini CLI)*
