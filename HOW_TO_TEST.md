# 시설 검색 API 테스트 가이드

## ✅ 구현 완료 확인 방법

### 1. 빌드 상태 확인

터미널에서 다음 명령 실행:
```powershell
.\gradlew.bat build -x test
```

**성공 시:**
```
BUILD SUCCESSFUL
```

### 2. 애플리케이션 실행

#### 방법 A: IDE에서 실행
- `OswcApplication.java` 클래스에서 Run
- 또는 에디터에서 우클릭 → Run 'OswcApplication.main()'

#### 방법 B: 터미널에서 실행
```powershell
.\gradlew.bat bootRun
```

### 3. Swagger UI로 테스트

1. 브라우저 접속: **http://localhost:8080/swagger-ui.html**

2. 아래로 스크롤하여 `facilities` 섹션 찾기

3. `GET /facilities/search` 클릭

4. "Try it out" 버튼 클릭

5. 파라미터 입력:
   ```
   latitude: 37.5665
   longitude: 127.0308
   radius: 5000
   page: 0
   size: 20
   ```

6. "Execute" 버튼 클릭

**성공하면:**
- Status 200 OK
- Response에 시설 목록 반환
- 서울 지역의 병원/약국 데이터 표시

### 4. PowerShell로 직접 테스트

```powershell
# Health Check
Invoke-RestMethod -Uri "http://localhost:8080/health"

# 시설 검색 (기본)
$url = "http://localhost:8080/facilities/search?latitude=37.5665&longitude=127.0308&radius=5000"
Invoke-RestMethod -Uri $url | ConvertTo-Json

# 병원만 검색
$url = "http://localhost:8080/facilities/search?latitude=37.5665&longitude=127.0308&type=HOSPITAL&radius=5000"
Invoke-RestMethod -Uri $url | ConvertTo-Json

# 약국만 검색
$url = "http://localhost:8080/facilities/search?latitude=37.5665&longitude=127.0308&type=PHARMACY&radius=5000"
Invoke-RestMethod -Uri $url | ConvertTo-Json
```

### 5. 데이터 확인

MongoDB에서 시설 데이터 확인:

```javascript
// MongoDB Shell에서
use oswc;
db.facilities.find().pretty();
```

예상 결과: 10개의 시설 데이터 (병원 5개, 약국 5개)

---

## 📊 성공 기준

### ✅ 모든 항목이 성공해야 함:

1. [ ] 빌드 성공 (`BUILD SUCCESSFUL`)
2. [ ] 애플리케이션 실행 (`http://localhost:8080/health` 접속 가능)
3. [ ] Swagger UI 접속 가능
4. [ ] 시설 검색 API 호출 성공
5. [ ] 시설 데이터 반환 (0개 이상)
6. [ ] 페이징 정보 포함 (totalElements, currentPage 등)

### 🎯 예상 결과

```json
{
  "data": {
    "content": [
      {
        "id": "...",
        "name": "서울대학교병원",
        "type": "HOSPITAL",
        "latitude": 37.5665,
        "longitude": 127.0308,
        "address": "서울특별시 종로구 대학로 101",
        "phone": "02-2072-2114",
        "isOperating": true
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "currentPage": 0,
    "size": 20
  }
}
```

---

## 🐛 문제 해결

### 애플리케이션이 실행되지 않는 경우
- MongoDB가 실행 중인지 확인
- Redis가 실행 중인지 확인 (선택사항)
- 포트 8080이 사용 중인지 확인

### 시설 데이터가 0개인 경우
- 애플리케이션 로그 확인
- `FacilityDataSeeder` 클래스 실행 여부 확인
- MongoDB 연결 상태 확인

### API 에러가 발생하는 경우
- HTTP 400: 파라미터 검증 실패 (위도/경도 확인)
- HTTP 404: MongoDB 연결 실패
- HTTP 500: 서버 오류 (로그 확인)

---

## 📁 구현된 파일

- `FacilityDocument.java` - 시설 엔티티
- `FacilityType.java` - 시설 유형 enum
- `FacilityRepository.java` - MongoDB 저장소
- `FacilitySearchRequest.java` - 요청 DTO
- `FacilityResponse.java` - 응답 DTO
- `FacilityService.java` - 비즈니스 로직
- `FacilitiesController.java` - REST API
- `FacilityDataSeeder.java` - 샘플 데이터 생성

---

## 📖 추가 정보

자세한 API 사용법: `FACILITY_API_GUIDE.md`  
테스트 스크립트: `test_facility_search.ps1`

