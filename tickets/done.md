# 🎫 Ticket: 연속 정답(콤보) 시스템 도입

## 📌 상태 (Status)
- **ID**: TKT-20260317-01
- **담당자**: Kero (Android Dev)
- **우선순위**: High
- **상태**: Done (Build-Verified)

## 📖 요구사항 (Requirements)
- 퀴즈 도중 2개 이상 연속 정답 시 콤보 시스템 활성화.
- 2연속 정답부터 문제당 +1 XP 추가 보너스 지급.
- 콤보 발생 시 "연속 n정답! (+1 XP)" 애니메이션 노출.
- 오답 시 콤보 카운트 초기화.

## 🛠️ 작업 내역 (Tasks)
- [x] `QuizContract.kt`: State에 `comboCount` 추가 및 SideEffect에 `ShowComboEffect` 추가.
- [x] `QuizViewModel.kt`: 정답 판정 로직에 콤보 카운팅 및 XP 보너스 계산 로직 구현.
- [x] `QuizScreen.kt`: `AnimatedVisibility`를 이용한 콤보 UI 컴포넌트 추가 및 사이드 이펙트 연동.
- [x] `REQUIREMENTS.md`: 콤보 시스템 명문화.
- [x] `ROADMAP.md`: M25 항목 업데이트 및 완료 처리.

## ✅ 검증 결과 (Verification)
- `./gradlew assembleDebug` 빌드 성공 (38s).
- 로직 및 UI 구현 완료.
