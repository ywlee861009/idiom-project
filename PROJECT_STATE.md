# 🗺️ PROJECT STATE: 사자성어 퀴즈 (Idiom Quiz)

> **AI 주의사항**: 새로운 세션이나 환경에서 이 프로젝트를 처음 만났다면, **이 파일을 가장 먼저 읽으십시오.** 이 파일은 프로젝트의 모든 맥락을 연결하는 마스터 가이드입니다.

---

## 1. 핵심 페르소나 (Personas)
- **PM (Mark)**: 프로젝트 기획, 일정 관리, `REQUIREMENTS.md`, `ROADMAP.md` 담당.
- **Android Dev (Kero)**: 시니어 안드로이드 개발자. MVI, Clean Architecture, 멀티모듈 전문. `GUIDE_FOR_ANDROID.md` 담당.
- **Designer (Jenny)**: 10년차 시니어 프로덕트 디자이너. Extreme Minimalism 및 UI/UX 설계. `GUIDE_FOR_DESIGNER.md` 담당.

## 2. 프로젝트 정체성 (Identity)
- **목표**: 안드로이드 MVP 사자성어 퀴즈 앱 개발.
- **컨셉**: Extreme Minimalism (Material 3 활용).
- **기술 스택**: Kotlin (1.9.22), Jetpack Compose, Hilt, Coroutines, Repository Pattern.
- **데이터**: 200개의 사자성어 JSON 내장 (`assets/idioms.json`).

## 3. 현재 진행 상황 (Current Status - 2026-03-08)
- [x] **Phase 0**: 아이디어 구체화 및 마스터 가이드 구축 완료.
- [x] **Phase 1 (Data)**: 200개 사자성어 데이터셋 구축 완료 (`ID-02`).
- [x] **Phase 2 (Scaffolding)**: 안드로이드 프로젝트 구조 및 멀티모듈 셋업 완료 (`ID-01`).
- [x] **Phase 3 (Core Implementation)**: 도메인 모델, 데이터 파싱, 퀴즈 엔진 알고리즘 및 메인 퀴즈 화면 구현 완료 (`ID-03`, `ID-04`, `ID-05`, `ID-06`, `ID-07`, `ID-08`).

## 4. 필수 참조 문서 (Core Documents)
상세한 맥락 파악을 위해 다음 순서로 문서를 읽으십시오:
1.  **`README.md`**: 프로젝트 전체 소개 및 실행 방법.
2.  **`REQUIREMENTS.md`**: 상세 기능 명세 및 디자인 가이드.
3.  **`ROADMAP.md`**: 전체 개발 마일스톤 및 일정.
4.  **`tickets/`**: 
    - `todo.md`: 현재 대기 중인 티켓 (ID-09, ID-10).
    - `done.md`: 완료된 티켓 히스토리 (ID-01 ~ ID-08).
5.  **`GUIDE_FOR_ANDROID.md`**: 개발자 케로의 설계 철학 및 멀티모듈 구조 상세.
6.  **`GUIDE_FOR_DESIGNER.md`**: 디자이너 제니의 디자인 철학 및 UI/UX 가이드라인.

---

## 🚀 다음 작업 (Next Action)
- **ID-09: Result Screen Enhancement** (결과 화면 고도화)
- **ID-10: Local Data Storage Integration** (최고 점수 저장 및 로컬 연동)

---
*Last Sync: 2026-03-08*  
*Sync by: PM Mark & Dev Kero*
