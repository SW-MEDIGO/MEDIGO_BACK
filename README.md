# MEDIGO Backend (OSWC)

메디고(MEDIGO) 백엔드는 Spring Boot 기반의 RESTful API 서버입니다. 본 저장소는 오픈소스 프로그래밍 대회(오픈소스 SW 경진대회) 출품용으로 구성되었으며, 사용자 인증/권한, 시설 검색, 예약, 리뷰, 실시간 추적, 관리자 가용 시간 관리 등의 도메인을 포함합니다.

- **Framework**: Spring Boot 3.3.x (Java 17)
- **Database**: MongoDB
- **Cache/Token Store**: Redis (Refresh 토큰 로테이션/블랙리스트)
- **Auth**: JWT (Access/Refresh)
- **API 문서**: Swagger UI 및 노션 명세 활용

## 목차
- [주요 기능](#주요-기능)
- [프로젝트 구조](#프로젝트-구조)
- [로컬 실행 방법](#로컬-실행-방법)
- [환경 변수 및 설정](#환경-변수-및-설정)
- [API 문서](#api-문서)
- [빌드/테스트](#빌드테스트)
- [개발 가이드](#개발-가이드)
- [라이선스](#라이선스)

## 주요 기능
- **인증/인가**: 이메일/비밀번호 로그인, JWT 발급 및 갱신, 로그아웃
- **내 정보**: `me` 엔드포인트를 통한 사용자 정보 조회/수정
- **시설(facility)**: 시설 등록/검색/상세 조회
- **예약(reservation)**: 예약 생성/상태 변경/이벤트 트래킹
- **리뷰(review)**: 리뷰 작성/조회
- **실시간 추적(tracking)**: 진행 상태(TrackingStatus) 업데이트
- **관리자(manager)**: 가용 시간 관리, 관리자 관련 API

## 프로젝트 구조
루트 기준 주요 디렉터리/파일 설명입니다.

```
src/main/java/com/example/oswc/
  auth/                 # 인증/인가(JWT, 필터, 리프레시 토큰 서비스)
  common/               # 공통 응답, 예외, 파일 업로드/알림 등
  config/               # 보안, 웹, OpenAPI 설정
  facility/             # 시설 도메인
  manager/              # 관리자/가용시간 도메인
  reservation/          # 예약/이벤트 도메인
  review/               # 리뷰 도메인
  tracking/             # 실시간 상태 추적 도메인
  user/                 # 사용자 도메인

src/main/resources/
  application.yml       # 로컬/기본 설정

build.gradle.kts        # Gradle 빌드 스크립트
README.md               # 현재 문서
```

## 로컬 실행 방법
사전 요구사항:
- Java 17
- Gradle Wrapper 포함 (동봉된 `gradlew` 사용 권장)
- MongoDB (기본: `mongodb://localhost:27017/oswc`)
- Redis (기본: `localhost:6379`)

1) 의존성 설치 및 빌드
```bash
./gradlew clean build
```

2) 애플리케이션 실행
```bash
# 개발 모드 실행
./gradlew bootRun

# 또는 빌드된 JAR 실행
java -jar build/libs/oswc-backend-0.0.1-SNAPSHOT.jar
```

실행 후 기본 포트는 `http://localhost:8080` 입니다.

## 환경 변수 및 설정
기본 설정은 `src/main/resources/application.yml`을 참고하세요. 운영/개발 환경에 따라 아래 값을 환경변수 또는 외부 설정으로 재정의하세요.

- **MongoDB**: `spring.data.mongodb.uri` (예: `mongodb://localhost:27017/oswc`)
- **Redis**:
  - `spring.data.redis.host` (기본: `localhost`)
  - `spring.data.redis.port` (기본: `6379`)
- **서버 포트**: `server.port` (기본: `8080`)
- **파일 업로드 경로**: `app.upload.dir` (기본: `uploads` → `/files/**`로 정적 매핑)
- **JWT**:
  - `app.jwt.secret` (필수: 32자 이상 랜덤 문자열로 교체 필요)
  - `app.jwt.access-exp-min` (기본: 30분)
  - `app.jwt.refresh-exp-days` (기본: 14일)
- **FCM**:
  - `fcm.enabled` (기본: false)
  - `fcm.server-key`

예시(Windows PowerShell) - 안전한 JWT 시크릿 생성:
```powershell
[guid]::NewGuid().ToString('N') + [guid]::NewGuid().ToString('N')
```

## API 문서
- **Swagger UI**: 애플리케이션 실행 후 브라우저에서 확인 (예: `/swagger-ui/index.html`)
- **노션 명세서**: 오픈소스 SW 경진대회 API 명세서 — 링크: `https://www.notion.so/SW-API-28dfa01340108066b8efd49d5ac85405`

또한 저장소 루트의 문서를 참고하세요.
- `FACILITY_API_GUIDE.md`: 시설 API 가이드
- `FACILITY_API_CHANGES.md`: 시설 API 변경 이력
- `TEST_RESERVATION_API.md`: 예약 API 테스트 시나리오
- `HOW_TO_TEST.md`: 로컬 테스트/검증 방법

## 빌드/테스트
- 빌드: `./gradlew build`
- 테스트: `./gradlew test`
- 산출물: `build/libs/oswc-backend-0.0.1-SNAPSHOT.jar`

PowerShell 스크립트(Windows 로컬 편의):
- `test_reservation_api.ps1`, `test_facility_search.ps1`, `verify_api.ps1`, `run_cancel_test.ps1`

## 개발 가이드
- 코드 스타일: 명확한 네이밍, 계층 분리, 불필요한 예외 포착 지양
- 설정 분리: 민감정보는 환경변수로 주입 (`application.yml`는 예시 기본값)
- 보안: 운영 환경에서는 `org.springframework.security` 로깅 레벨을 `INFO`로 조정 권장
- 데이터 시드: `FacilityDataSeeder` 등을 통해 초기 데이터 주입 가능

## 라이선스
본 프로젝트는 오픈소스 경진대회 제출용 예시이며, 별도 명시가 없는 한 Apache-2.0 또는 MIT 중 하나를 권장합니다. 실제 공개 시 저장소 루트의 `LICENSE` 파일을 확인/갱신해주세요.
