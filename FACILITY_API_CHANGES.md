# 시설 검색 API 변경사항 (명세서 반영)

## 📋 주요 변경사항

### 1. 요청 파라미터 변경

| 이전 | 변경 후 | 설명 |
|------|---------|------|
| `isOperating` | `open_now` | 현재 운영중인 곳만 필터링 (명세서 기준) |
| `page: 0-based` | `page: 1-based` | 페이지 번호가 1부터 시작 |
| `size` | `limit` | 페이지 크기 파라미터명 변경 |
| 기본값 변경 | | `radius: 3000m`, `page: 1`, `limit: 10` |
| `type` 기본값 | `HOSPITAL` | 기본값 추가 |

### 2. 응답 구조 변경

**이전:**
```json
{
  "data": {
    "content": [...],
    "totalElements": 10,
    "currentPage": 0,
    "size": 20
  }
}
```

**변경 후 (명세서 기준):**
```json
{
  "data": {
    "pagination": {
      "currentPage": 1,
      "totalPages": 2,
      "totalItems": 25
    },
    "facilities": [
      {
        "id": "A1234567",
        "name": "서울대학교병원",
        "type": "HOSPITAL",
        "address": "서울특별시 종로구 대학로 101",
        "phone": "02-2072-2114",
        "location": {
          "latitude": 37.5665,
          "longitude": 127.0308
        },
        "distanceMeters": 1500,
        "isOpen": true,
        "operatingHoursToday": "09:00 - 18:00"
      }
    ]
  }
}
```

### 3. 응답 필드 변경

**FacilityResponse 주요 변경:**
- ✅ `location` 객체로 latitude, longitude 묶기
- ✅ `distanceMeters` (Integer, 미터 단위)
- ✅ `isOpen` (Boolean)
- ✅ `operatingHoursToday` (String)
- ❌ 삭제: `latitude`, `longitude` (위 최상위)
- ❌ 삭제: `detailAddress`
- ❌ 삭제: `isOperating`
- ❌ 삭제: `openTime`, `closeTime`
- ❌ 삭제: `openToday`
- ❌ 삭제: `distance` (Double)

## 🔧 사용 예시

### 요청 파라미터

```bash
GET /facilities/search?latitude=37.5665&longitude=127.0308&type=HOSPITAL&open_now=false&radius=3000&page=1&limit=10
```

**파라미터 설명:**
- `latitude` (필수): 위도
- `longitude` (필수): 경도
- `type` (선택): HOSPITAL 또는 PHARMACY (기본값: HOSPITAL)
- `open_now` (선택): true/false (기본값: false)
- `radius` (선택): 반경 미터 (기본값: 3000)
- `page` (선택): 페이지 번호, 1부터 시작 (기본값: 1)
- `limit` (선택): 페이지 크기 (기본값: 10)

### 성공 응답 (200 OK)

```json
{
  "status": "success",
  "data": {
    "pagination": {
      "currentPage": 1,
      "totalPages": 2,
      "totalItems": 25
    },
    "facilities": [
      {
        "id": "facility_id_1",
        "name": "서울대학교병원",
        "type": "HOSPITAL",
        "address": "서울특별시 종로구 대학로 101",
        "phone": "02-2072-2114",
        "location": {
          "latitude": 37.5665,
          "longitude": 127.0308
        },
        "distanceMeters": 1500,
        "isOpen": true,
        "operatingHoursToday": "09:00 - 18:00"
      }
    ]
  }
}
```

### 에러 응답

#### 400 Bad Request
```json
{
  "status": "error",
  "error": {
    "code": "MISSING_REQUIRED_PARAMS",
    "message": "필수 파라미터(latitude, longitude)가 누락되었습니다."
  }
}
```

#### 401 Unauthorized
```json
{
  "status": "error",
  "error": {
    "code": "TOKEN_EXPIRED",
    "message": "Access Token이 만료되었습니다."
  }
}
```

## ✅ 완료된 작업

1. ✅ 요청 파라미터명 변경 (`isOperating` → `open_now`)
2. ✅ 페이징 방식 변경 (1-based)
3. ✅ 응답 구조 변경 (pagination + facilities)
4. ✅ LocationInfo 중첩 객체 추가
5. ✅ 응답 필드명 변경 (distanceMeters, isOpen 등)
6. ✅ 빌드 성공 확인

## 📝 다음 단계

1. 애플리케이션 재실행 후 테스트
2. Swagger UI에서 검증: http://localhost:8080/swagger-ui.html
3. 파라미터 및 응답 구조 확인

## 🎯 명세서 일치 여부

| 항목 | 상태 |
|------|------|
| 엔드포인트 | ✅ GET /facilities/search |
| 파라미터명 | ✅ open_now, page, limit, radius |
| 페이지 번호 | ✅ 1-based |
| 응답 구조 | ✅ pagination + facilities |
| location 객체 | ✅ 중첩 구조 |
| distanceMeters | ✅ Integer |
| isOpen | ✅ Boolean |
| operatingHoursToday | ✅ String |

---

**참고:** API 명세서 기준으로 완전히 일치하도록 수정되었습니다.

