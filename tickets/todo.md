# 📋 To Do Tickets (백로그)

---

# [TKT-M26] Google Play Billing 인앱 결제 연동

## Description
Google Play Billing Library를 연동하여 유료 테마 팩(2,000원) 구매 기능을 구현한다.

## Acceptance Criteria
- [ ] BillingClient 초기화 및 연결 처리
- [ ] 테마 팩 상품(Product) 등록 및 조회
- [ ] 구매 플로우 실행 및 결과 처리
- [ ] 구매 상태 로컬 DB 저장 (Realm 또는 DataStore)

## Technical Notes
- Google Play Billing Library 최신 버전 사용
- 테마 팩 가격: 2,000원 (KRW)
- 구매 검증은 로컬 처리 (서버 없음)

## Priority
High

---

# [TKT-M27] 테마 잠금/해제 로직

## Description
유료 테마에 대한 잠금 상태 관리 및 구매 후 해제(Unlock) 로직을 구현한다.

## Acceptance Criteria
- [ ] 테마 잠금 상태 모델 정의 (Domain Layer)
- [ ] 서고(Collection) 화면에 잠긴 테마 팩 리스트 노출
- [ ] 구매 완료 시 테마 잠금 해제 및 UI 반영
- [ ] 앱 재시작 시 구매 상태 복원

## Technical Notes
- M26(Billing) 완료 후 진행
- DataStore에 구매 상태 persist

## Priority
High

---

# [TKT-M28] 서고 상세 TTS 음성 지원

## Description
서고 상세 바텀시트에서 성어의 독음 및 뜻풀이를 음성(TTS)으로 읽어주는 기능을 추가한다.

## Acceptance Criteria
- [ ] TTS 엔진 초기화 (Android TextToSpeech API)
- [ ] 바텀시트에 음성 재생 버튼 추가
- [ ] 한국어 TTS로 독음 + 뜻풀이 읽기
- [ ] TTS 리소스 정리 (DisposableEffect)

## Technical Notes
- Android 내장 TTS vs 외부 API 결정 필요 (미결정 사항)
- Compose Multiplatform expect/actual 패턴 적용

## Priority
Medium

---

# [TKT-M28-1] 순서 맞히기 퀴즈 타입 구현

## Description
성어의 글자를 셔플하여 올바른 순서로 배열하는 "순서 맞히기" 퀴즈 타입을 구현한다.

## Acceptance Criteria
- [ ] QuizType enum에 ORDER_MATCH 타입 추가
- [ ] GetRandomQuizUseCase에 순서 맞히기 생성 로직 추가
- [ ] 드래그 또는 탭 기반 순서 배열 UI 구현
- [ ] 정답 판정 로직 구현
- [ ] 기존 테스트에 ORDER_MATCH 케이스 추가

## Technical Notes
- 4글자 성어 기준 셔플 → 사용자가 올바른 순서로 재배열
- UI: 드래그앤드롭 또는 탭하여 순서 선택 방식 중 택 1

## Priority
Medium

---

# [TKT-M29] Visual Polish 최종 검수

## Description
전반적인 폰트 가독성(20sp 이상) 최종 검수 및 애니메이션 튜닝을 수행한다.

## Acceptance Criteria
- [ ] 모든 화면의 텍스트가 20sp 이상인지 검증
- [ ] 버튼/터치 영역 최소 크기 검증 (48dp 이상)
- [ ] 먹물 애니메이션 타이밍 최적화
- [ ] 콤보 애니메이션 자연스러움 검증

## Technical Notes
- Silver-friendly UX 기준 적용 (40대+ 타겟)
- Accessibility 가이드라인 참고

## Priority
Medium

---

# [TKT-M30] 스토어 에셋 준비

## Description
Google Play Store 등록을 위한 스크린샷, 아이콘, 설명문 등 에셋을 준비한다.

## Acceptance Criteria
- [ ] 앱 아이콘 최종 버전 확정
- [ ] 스토어 스크린샷 캡처 (최소 4장)
- [ ] 앱 제목, 간단한 설명, 상세 설명 작성
- [ ] Feature Graphic(1024x500) 제작

## Technical Notes
- 한국어 우선, 영어 버전은 후순위
- "The Calm Ink" 브랜딩 일관성 유지

## Priority
Medium

---

# [TKT-M31] Release Build 검증 및 배포

## Description
Proguard(R8) 적용된 Release AAB 빌드를 검증하고 Google Play Console에 배포한다.

## Acceptance Criteria
- [ ] R8 minification 적용 빌드 성공
- [ ] Release 빌드에서 전체 기능 동작 검증
- [ ] Crashlytics 연동 확인 (난독화 매핑 파일 업로드)
- [ ] Google Play Console 내부 테스트 트랙 업로드

## Technical Notes
- `CLAUDE.md`의 deploy 절차 준수
- AAB 파일 명명: `app-{versionName}-{versionCode}.aab`

## Priority
High
