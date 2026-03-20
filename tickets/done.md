# 🎫 Ticket: [Quiz] 주관식 입력 포커스 UX 개선

## 📌 상태 (Status)
- **ID**: TKT-20260320-01
- **담당자**: Kero (Android Dev)
- **우선순위**: High
- **상태**: Done (Build-Verified)

## 📖 요구사항 (Requirements)
- 주관식 퀴즈 입력 시 한글 조합 중에도 포커스가 다음 칸으로 넘어가는 UX 문제 해결.
- 한 글자가 온전히 완성될 때까지(조합이 끝날 때까지) 현재 칸에 포커스를 유지.

## 🛠️ 작업 내역 (Tasks)
- [x] `QuizScreen.kt`: `BasicTextField`의 `value`를 `String`에서 `TextFieldValue`로 변경하여 `composition` 상태 관리.
- [x] `QuizScreen.kt`: `LaunchedEffect`를 추가하여 외부 상태(`inputText`) 변경 시 내부 상태(`TextFieldValue`) 동기화 로직 구현.
- [x] `QuizScreen.kt`: `isCurrent` 계산 로직에 `composition` 여부를 반영하여 조합 중 포커스 이동 방지.

## ✅ 검증 결과 (Verification)
- 코드 리뷰 및 논리 검증 완료.
- `BasicTextField`의 `TextFieldValue` 사용 및 `composition` 체크 로직 적용.

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
