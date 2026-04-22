## deploy 명령

사용자가 **"deploy"** 라고 입력하면 아래 절차를 순서대로 수행한다.

### 1. 버전 타입 선택
`major / minor / patch` 중 어떤 것을 올릴지 사용자에게 묻는다.

### 2. 버전 번호 업데이트 (`app/build.gradle.kts`)
- 현재 `versionName`(semver) 과 `versionCode`(정수)를 읽는다.
- 선택에 따라 versionName 세그먼트 증가 (major → X+1.0.0 / minor → x.Y+1.0 / patch → x.y.Z+1)
- `versionCode` 는 무조건 +1
- 파일을 직접 수정한다.

### 3. 서명된 AAB 빌드
keystore 정보는 `keystore/keystore-info.md` 에서 읽어온다.

```bash
./gradlew bundleRelease \
  -Pandroid.injected.signing.store.file="$(pwd)/keystore/kero-studio.jks" \
  -Pandroid.injected.signing.store.password=<StorePassword> \
  -Pandroid.injected.signing.key.alias=<KeyAlias> \
  -Pandroid.injected.signing.key.password=<KeyPassword>
```

### 4. AAB 이동 및 이름 변경
빌드 결과물(`app/build/outputs/bundle/release/app-release.aab`)을
프로젝트 루트로 이동하며 이름을 변경한다.

```
app-{versionName}-{versionCode}.aab
```

예시: `app-1.2.0-7.aab`
