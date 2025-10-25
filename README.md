> 개발자의 성장과 취업을 지원하는 AI 기반 통합 플랫폼, **DevStation**

## 💡서비스 소개

**DevStation**은 개발자들이 함께 성장할 수 있도록 돕는 플랫폼입니다.

커뮤니티를 통해 팀 프로젝트와 스터디를 모집하고,

OpenAI 기반 AI 면접 시스템을 통해 기술 면접을 준비하며,

Toss Payments를 통한 구독 서비스를 통해 프리미엄 서비스를 이용할 수 있습니다.

---

## **🎯 서비스 목표**

1. **개발자 간 협업 및 네트워킹 촉진**
    - 스터디 및 프로젝트 팀 매칭 기능
    - 게시판을 통한 커뮤니티 형성
2. **AI를 통한 맞춤형 면접 준비**
    - 포트폴리오 기반 기술 면접 질문 제공
    - 답변 피드백 및 꼬리질문 자동 생성
3. **지속 가능한 구독 기반 서비스 운영**
    - 유료 구독자를 위한 프리미엄 AI 기능 제공
    - Toss Payments 자동결제 연동
      
---

## 🧑‍💻 팀원 소개

| 김은경 | 김규섭 | 서명진 | 이유찬 | 조영주 | 최병준 |
| :---: | :---: | :---: | :---: | :---: | :---: |
| [github.com/kimeunkyoungg](https://github.com/kimeunkyoungg) | [github.com/seopgyu](https://github.com/seopgyu) | [github.com/myoungjinseo](https://github.com/myoungjinseo) | [github.com/Labtory-82](https://github.com/Labtory-82) | [github.com/ascal34](https://github.com/ascal34) | [github.com/larama-C](https://github.com/larama-C) |
| 팀장 | 팀원 | 팀원 | 팀원 | 팀원 | 팀원 |

---

## 🧩 핵심 기능

### 🧑‍💻 1. **회원 관리 시스템 (JWT 인증 기반)**

- **회원가입 / 로그인 / 로그아웃**
    - Spring Security + JWT 기반 인증
    - Access Token / Refresh Token 분리 및 재발급 전략
- **권한 관리**
    - 일반 사용자 / 관리자(Role-based Access)
- **비밀번호 암호화**
    - BcryptPasswordEncoder 적용
- **회원 정보 수정**

---

### 📰 2. **게시판 및 커뮤니티 기능**

- **게시글 CRUD**
    - 팀 프로젝트 / 스터디 모집 게시판
- **댓글 CRUD**
    - 수정/삭제 기능 지원
- **게시글 필터링 / 페이징**
    - 카테고리별, 상태별 조회
- **모집글 마감 기한 관리**
- **유료 기능**
    - Toss Payments 결제 후, **모집글 상단 고정 기능** 제공

---

### 🤖 3. **AI 면접 지원 시스템 (OpenAI API 기반)**

- **CS 면접 질문 제공**
- **AI 질문 생성**
    - 포트폴리오 기반 맞춤형 질문 생성
    - 꼬리 질문 자동 생성
- **AI 피드백**
    - 사용자의 면접 답변 분석 및 피드백 제공
- **오늘의 추천 문제**
    
    
- **QnA 게시판**
    - AI 학습 질문 및 답변 공유 커뮤니티

---

### 🏆 4. **랭킹 시스템**

- **사용자별 랭킹 및 티어 시스템**
    - AI 면접 점수 기반 자동 랭킹 산정
- **리더보드 제공**
    - 상위 유저 노출 및 경쟁 유도
- **마이페이지 연동**
    - 본인의 랭킹 및 등급 조회 가능

---

### 💬 5. **QnA 게시판**

- **서비스 관련 질문 및 학습 커뮤니티**
    - 사용자가 질문을 등록하고 답변 공유 가능
- **태그 및 카테고리 기반 검색**
    - 주제별로 질문 필터링 및 조회

---

### 💳 6. **결제 및 구독 서비스 (Toss Payments 연동)**

- **자동결제 시스템**
    - 카드 등록 후 첫 결제 → 매월 자동 결제
- **결제 내역 관리**
    - 구독 이력 및 상태 확인
- **구독 혜택**
    - 포트폴리오 기반 질문 횟수 증가
    - AI 첨삭 서비스 이용 가능
    - 모집글 상단 고정 기능

---

### 🧠 7. **마이페이지**

- 내 랭킹 / 티어 확인
- 내가 작성한 글 / 댓글 / 문제 조회
- 포트폴리오 및 이력서 등록
- 개인정보 수정
- 유료 서비스 관리

---

### 🛠️ 8. **관리자 페이지**

- 사용자 목록 및 상태 관리
- 게시글 및 댓글 관리
- 질문 / 피드백 데이터 관리
- 결제 내역 조회 및 통계

---

## ⚙️ 환경 변수 설정

BACKEND (IntelliJ 환경 변수 설정)

```java
# Database
DB_JDBC_URL=jdbc:mysql://localhost:3306/dev_station
DB_USER=your_db_user
DB_PASSWORD=your_db_password

# Email
EMAIL_ID=your_email@gmail.com
EMAIL_PW=your_email_app_password

# OpenAI
OPENAI_API_KEY=your_openai_api_key
OPENAI_API_URL=https://api.openai.com/v1/chat/completions

# Payment
PAYMENT_SECRET_KEY=your_payment_secret_key

# JWT
SECRET_PATTERN=your_secret_pattern
```

FRONTEND (.env)

```
NEXT_PUBLIC_API_BASE_URL ="http://localhost:8080"

# Payment
NEXT_PUBLIC_TOSS_CLIENT_KEY = your_payment_secret_key
```

---

## 🧾 API 명세서

 http://localhost:8080/swagger-ui/index.html

---

## ☁️ 시스템 아키텍처

---

## 🧱 기술 스택
### 💻 **Frontend**

![Next.js](https://img.shields.io/badge/Next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![TailwindCSS](https://img.shields.io/badge/TailwindCSS-38B2AC?style=for-the-badge&logo=tailwindcss&logoColor=white)


### ⚙️ **Backend**

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-007396?style=for-the-badge&logo=hibernate&logoColor=white)
![QueryDSL](https://img.shields.io/badge/QueryDSL-4479A1?style=for-the-badge&logo=databricks&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2%20Database-003B57?style=for-the-badge&logo=h2&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![OpenAI API](https://img.shields.io/badge/OpenAI%20API-412991?style=for-the-badge&logo=openai&logoColor=white)
![Toss Payments](https://img.shields.io/badge/Toss%20Payments-0064FF?style=for-the-badge&logo=toss&logoColor=white)
![Gmail SMTP](https://img.shields.io/badge/Gmail%20SMTP-EA4335?style=for-the-badge&logo=gmail&logoColor=white)

---

## 🤖 Github Actions CI 자동화 Test

### 🧩 Workflow 개요

- **트리거 조건**
    - 브랜치: `main`, `feature/*`, `fix/*`, `refactor/*`
    - 경로: `backend/**`
- **실행 환경**
    - JDK 21
- **CI 단계**
    1. 저장소 체크아웃
    2. Java 환경 세팅
    3. Gradle 실행 권한 부여
    4. 테스트 프로파일(`spring.profiles.active=test`)로 빌드 및 테스트 실행
    5. 테스트 결과를 PR 코멘트로 출력
    6. 실패한 코드 라인에 체크 코멘트 등록

---

## 💬 개발 컨벤션

### 🚀 GitHub Flow

- **main**
    - 실제 서비스에 배포되는 안정화 브랜치
    - 직접 커밋 금지 (feature, fix, refactor 브랜치를 통해 반영)
    - 브랜치 보호 규칙 적용 : PR을 통해 최소 1명의 리뷰 승인 후 머지 가능
- **feature/ & fix/ & refactor/**
    - 개별 기능 개발, 버그 수정, 코드 리팩토링용 브랜치
    - 이슈 단위로 생성하여 작업
    - 작업 완료 후 PR을 통해 main에 머지
    

---

### **🔄 작업 순서**

1. **이슈 생성** → 작업 단위 정의
2. **브랜치 생성** → main 브랜치에서 이슈별 작업 브랜치 생성
3. **Commit & Push**
4. **PR 생성 & 코드 리뷰** → 최소 1명 승인 필요
5. **Merge & 브랜치 정리**
    - 리뷰 완료 후 main 브랜치로 Merge
    - Merge 후 이슈별 작업 브랜치 삭제

---

### ⚙️ 네이밍 & 작성 규칙

1. **이슈**
    - 제목 규칙 : `[타입] 작업내용`
    - 예시 : `[feat] 로그인 기능 추가`
    - 본문은 템플릿에 맞춰서 작성
2. **PR**
    - 제목 규칙 : `[타입] 작업내용`
    - 예시 : `[feat] 로그인 기능 추가`
    - 본문은 템플릿에 맞춰서 작성 + close #이슈넘버
3. **브랜치**
    - 생성 기준 : `main` 브랜치에서 생성
    - 명명 규칙  : `타입/#이슈번호`
    - 예시: `feature/#1`
      
4. **Commit Message 규칙**
    
    
    | 타입 | 의미 |
    | --- | --- |
    | **feat** | 새로운 기능 추가 |
    | **fix** | 버그 수정 |
    | **docs** | 문서 수정 (README, 주석 등) |
    | **style** | 코드 스타일 변경 (포맷팅, 세미콜론 등. 기능 변화 없음) |
    | **refactor** | 코드 리팩토링 (동작 변화 없음) |
    | **test** | 테스트 코드 추가/수정 |
    | **chore** | 빌드, 패키지 매니저, 설정 파일 등 유지보수 작업(환경 설정) |
    | **remove** | 파일, 폴더 삭제 |
    | **rename** | 파일, 폴더명 수정 |
    - `타입 : 작업내용 #이슈번호`
    - 예시: `feat : 로그인 기능 추가#1`
