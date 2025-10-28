# 시설 검색 API 사용 가이드

## 📍 엔드포인트

**GET `/facilities/search`** - 주변 병원/약국 검색

---

## 🔍 요청 파라미터

| 파라미터 | 타입 | 필수 | 설명 | 기본값 |
|---------|------|------|------|-------|
| `latitude` | Double | ✅ | 위도 (-90 ~ 90) | - |
| `longitude` | Double | ✅ | 경도 (-180 ~ 180) | - |
| `type` | String | ❌ | 시설 유형 (HOSPITAL, PHARMACY) | null (전체) |
| `isOperating` | Boolean | ❌ | 운영중 여부 | null (전체) |
| `radius` | Integer | ❌ | 반경 (미터, 100~50000) | 5000 |
| `page` | Integer | ❌ | 페이지 번호 (0부터) | 0 |
| `size` | Integer | ❌ | 페이지 크기 (1~100) | 20 |

---

## 📥 응답 형식

### 성공 응답 (200 OK)
```json
{
  "data": {
    "content": [
      {
        "id": "facility_id",
        "name": "서울대학교병원",
        "type": "HOSPITAL",
        "latitude": 37.5665,
        "longitude": 127.0308,
        "address": "서울특별시 종로구 대학로 101",
        "detailAddress": "",
        "phone": "02-2072-2114",
        "isOperating": true,
        "openTime": "09:00:00",
        "closeTime": "18:00:00",
        "openToday": true,
        "distance": 1500.5
      }
    ],
    "totalElements": 25,
    "totalPages": 2,
    "currentPage": 0,
    "size": 20,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

---

## 🧪 테스트 예제

### 1. Swagger UI에서 테스트

1. 브라우저에서 `http://localhost:8080/swagger-ui.html` 접속
2. `GET /facilities/search` 엔드포인트 찾기
3. "Try it out" 클릭
4. 파라미터 입력:
   ```
   latitude: 37.5665
   longitude: 127.0308
   type: HOSPITAL
   radius: 5000
   page: 0
   size: 20
   ```
5. "Execute" 클릭

### 2. cURL로 테스트

#### 기본 검색 (서울 중심, 5km 반경)
```bash
curl "http://localhost:8080/facilities/search?latitude=37.5665&longitude=127.0308&radius=5000"
```

#### 병원만 검색
```bash
curl "http://localhost:8080/facilities/search?latitude=37.5665&longitude=127.0308&type=HOSPITAL&radius=5000"
```

#### 운영중인 약국만 검색
```bash
curl "http://localhost:8080/facilities/search?latitude=37.5665&longitude=127.0308&type=PHARMACY&isOperating=true&radius=3000"
```

### 3. PowerShell로 테스트

```powershell
# 서울 중심에서 반경 5km 내 모든 시설 검색
$params = @{
    latitude = 37.5665
    longitude = 127.0308
    radius = 5000
    page = 0
    size = 20
}
Invoke-RestMethod -Uri "http://localhost:8080/facilities/search" -Method Get -Body $params

# 병원만 검색
$params['type'] = 'HOSPITAL'
Invoke-RestMethod -Uri "http://localhost:8080/facilities/search" -Method Get -Body $params

# 운영중인 약국 검색
$params['type'] = 'PHARMACY'
$params['isOperating'] = $true
Invoke-RestMethod -Uri "http://localhost:8080/facilities/search" -Method Get -Body $params
```

---

## 📍 샘플 데이터 위치

시스템에는 다음 위치에 대한 샘플 데이터가 포함되어 있습니다:

| 시설명 | 타입 | 위치 | 위도 | 경도 |
|-------|------|------|------|------|
| 서울대학교병원 | 병원 | 종로구 | 37.5665 | 127.0308 |
| 세브란스병원 | 병원 | 서대문구 | 37.5625 | 126.9398 |
| 서울아산병원 | 병원 | 송파구 | 37.5267 | 127.1033 |
| 강남메디팜 | 약국 | 강남구 | 37.4979 | 127.0276 |
| 바이오약국 | 약국 | 강남구 | 37.5012 | 127.0273 |
| 올림픽약국 | 약국 | 송파구 | 37.5153 | 127.1026 |

---

## 🎯 사용 시나리오

### 시나리오 1: 내 주변 병원 찾기
```bash
# 내 위치를 기준으로 3km 반경 내 병원만 검색
GET /facilities/search?latitude=37.5665&longitude=127.0308&type=HOSPITAL&radius=3000
```

### 시나리오 2: 긴급상황 - 24시간 운영 약국 찾기
```bash
# 운영중인 약국 검색 (24시간 약국 필터링 가능)
GET /facilities/search?latitude=37.5665&longitude=127.0308&type=PHARMACY&isOperating=true
```

### 시나리오 3: 페이징 처리
```bash
# 첫 페이지
GET /facilities/search?latitude=37.5665&longitude=127.0308&page=0&size=10

# 두 번째 페이지
GET /facilities/search?latitude=37.5665&longitude=127.0308&page=1&size=10
```

---

## ⚠️ 주의사항

1. **좌표 순서**: GeoJSON 표준에 따라 MongoDB는 `[경도, 위도]` 순서를 사용합니다.
   - 위도(latitude): 가로선 (남북)
   - 경도(longitude): 세로선 (동서)

2. **반경 단위**: 미터(m)
   - 100m = 0.1km
   - 5000m = 5km
   - 최대: 50000m = 50km

3. **성능**: MongoDB GeoSpatial 인덱스가 자동으로 생성되어 빠르게 검색됩니다.

4. **개발 환경**: 애플리케이션이 시작될 때 자동으로 샘플 데이터가 생성됩니다.

---

## 🛠️ 개발자 정보

**구현 완료 날짜:** 2025-10-27  
**API 경로:** `GET /facilities/search`  
**MongoDB Collection:** `facilities`  
**인덱스:** `location` (2dsphere)

