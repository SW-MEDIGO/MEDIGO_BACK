# 🏥 메디고(MEDIGO) Backend API

병원·약국 **동행 서비스 플랫폼**의 백엔드 API 서버입니다.  
혼자 병원을 방문하기 어려운 사용자를 검증된 **동행자(매니저)** 와 매칭하고,  
예약·결제·실시간 추적 등 전 과정을 안전하게 관리합니다.

---

## 🚀 주요 기능

- **회원/인증 관리**: 회원가입, 로그인, 토큰 재발급  
- **예약 시스템**: 병원 동행 예약 생성·조회·취소  
- **매니저 매칭**: 성별, 시간대, 지역 기반 자동 매칭  
- **실시간 추적**: 동행 중 매니저의 위치 및 상태 실시간 업데이트  
- **후기 시스템**: 완료된 서비스에 대한 사용자 후기 작성  
- **시설 검색**: 병원 및 약국 위치, 운영 여부 조회  

---

## 📋 API 엔드포인트

### 🔐 인증 API (`/api/v1/auth/`)
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `POST` | `/auth/signup` | 회원가입 |
| `POST` | `/auth/login` | 로그인 |
| `POST` | `/auth/find-username` | 아이디 찾기 |
| `POST` | `/auth/request-password-reset` | 비밀번호 재설정 요청 |
| `POST` | `/auth/refresh` | Access Token 재발급 |

---

### 🧑‍🤝‍🧑 회원/프로필 API (`/api/v1/users/`)
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `GET` | `/users/me` | 내 정보 조회 *(예정)* |
| `PATCH` | `/users/me` | 내 정보 수정 *(예정)* |

---

### 🗓️ 예약 API (`/api/v1/reservations/`)
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `GET` | `/reservations` | 예약 목록 조회 (기간·상태 필터) |
| `POST` | `/reservations` | 신규 예약 생성 |
| `GET` | `/reservations/{id}` | 예약 상세 조회 |
| `PATCH` | `/reservations/cancel/{id}` | 예약 취소 |
| `POST` | `/reservations/{id}/reviews` | 후기 작성 |

---

### 🌟 매니저(동행자) API (`/api/v1/managers/`)
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `GET` | `/managers/{id}` | 매니저 프로필 조회 |
| `GET` | `/managers/me/availability` | 내 활동 가능 정보 조회 |
| `PATCH` | `/managers/me/availability` | 내 활동 가능 정보 수정 |

---

### 📍 실시간 추적 API (`/api/v1/tracking/`)
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `GET` | `/tracking/reservations/{id}` | 실시간 동행 상태 조회 |
| `PATCH` | `/tracking/reservations/{id}/status` | 동행 상태 업데이트 |
| `PATCH` | `/tracking/reservations/{id}/location` | 매니저 위치 주기 업로드 |

---

### 💊 병원/약국 시설 API (`/api/v1/facilities/`)
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `GET` | `/facilities/search` | 주변 병원/약국 검색 |
| `GET` | `/facilities/{id}` | 병원/약국 상세 정보 *(예정)* |

---

### ⚙️ 시스템 API
| Method | Endpoint | 설명 |
|--------|-----------|------|
| `GET` | `/health` | 서버 상태 확인 |
| `GET` | `/docs` | Swagger UI 문서 |
| `GET` | `/api/v1/swagger.json` | API 스펙 JSON |

---

## 🧱 기술 스택

| 구성 요소 | 기술 |
|------------|------|
| **Framework** | Flask 3.x / (FastAPI 예정) |
| **Database** | SQLAlchemy + PostgreSQL |
| **Authentication** | Flask-JWT-Extended (Access / Refresh Token) |
| **Data Processing** | Pandas |
| **API Documentation** | Flask-RESTX / Swagger UI |
| **CORS** | Flask-CORS |
| **Background Tasks** | Celery (위치 추적 업데이트) |

---

## 📊 데이터 소스

- **공공데이터 포털**: 병원/약국 운영 정보 (보건복지부)  
- **제휴 데이터베이스**: 실시간 운영 여부 API  
- **내부 데이터베이스**: 매니저 인증 / 예약 / 결제 정보  
- **추가 데이터셋 예시**
  - `hospital_data.csv`: 병원 위치 및 운영 정보  
  - `pharmacy_data.csv`: 약국 정보  
  - `manager_profiles.csv`: 동행자 프로필 및 인증 내역  
  - `reservation_logs.csv`: 예약 이력 및 상태 로그  

---

## 🛠️ 설치 및 실행

```bash
# 1️⃣ 의존성 설치
pip install -r requirements.txt

# 2️⃣ 환경 변수 설정
export FLASK_ENV=development
export JWT_SECRET_KEY=your-secret-key
export DATABASE_URL=sqlite:///instance/app.db

# 3️⃣ 데이터베이스 초기화
flask db init
flask db migrate -m "Initial migration"
flask db upgrade

# 4️⃣ 서버 실행
python run_server.py
