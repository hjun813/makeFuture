# MakeWorld (취업 준비 올인원 대시보드)
이 프로젝트는 Vue.js (Frontend)와 Spring Boot (Backend)를 사용하여 구축한 신입/인턴 구직자를 위한 개인 맞춤형 취업 준비 플랫폼입니다.

## 1. 프로젝트 계기
신입, 인턴 채용 공고를 여러 사이트에서 확인하고, 학습 계획과 지원 이력을 별도로 관리하는 것에 비효율을 느꼈습니다. 채용 정보 아카이빙부터 일정 관리, 성장 기록까지 취업 준비의 전 과정을 데이터화하여 한곳에서 시각적으로 관리할 수 있는 '나만의 취업 비서 서비스'가 필요하여 개발하게 되었습니다.

## 2. 주요 기능
* 🔐  사용자 인증 (Security)
JWT 로그인: Spring Security와 JWT를 활용한 Stateless 인증 시스템을 구축했습니다.

* 데이터 격리: 로그인한 사용자별로 본인의 공고, 할 일, 기록만 조회하고 관리할 수 있습니다.

* 📅  채용 공고 관리 (Job Board)
공고 등록: URL, 회사명, 직무, 마감일, 채용 형태(신입/인턴) 등 공고 정보를 DB에 저장합니다.

* D-Day 계산: 마감일까지 남은 기간을 자동으로 계산하여 보여주며, 마감일 순으로 정렬됩니다.

* 모아보기: '내 공고'(본인 등록)와 '전체 공고'(모든 사용자 공유) 뷰를 제공합니다.

* ✅ 목표 중심 Todo List 
기간별 관리: '오늘', '이번 주', '이번 달' 탭으로 나누어 체계적인 일정 관리가 가능합니다.

* 인라인 수정: 리스트 내에서 즉시 내용을 수정하거나 상태(완료/미완료)를 변경할 수 있습니다.

* 진행률 시각화: 오늘의 할 일 달성도를 Progress Bar 형태로 대시보드에서 직관적으로 확인합니다.

*  📈 성장 히스토리 (History Timeline) 
타임라인 뷰: 매일의 학습 내용이나 활동을 타임라인 형태로 기록하여 성장 과정을 한눈에 파악합니다.

* 최신순 정렬: 가장 최근의 활동이 상단에 노출되도록 구현했습니다.

* 📊 통합 대시보드 
위젯 시스템: 로그인 시 마감 임박 공고, 오늘의 할 일, 최근 활동 기록을 한 화면에서 요약해 보여줍니다.

## 3. 기술 스택
[![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![Spring](https://img.shields.io/badge/Spring-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)

## 4. 프로젝트 구조

```Bash

.
├── backend/ (Spring Boot)
│   ├── src/main/java/com/example/backend/
│   │   ├── config/           # Security, CORS, Swagger 설정
│   │   ├── controller/       # API 엔드포인트 (Auth, Job, Todo, History)
│   │   ├── domain/           # JPA Entity (User, Job, Todo, History)
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── jwt/              # JWT 인증/인가 필터 및 유틸리티
│   │   ├── repository/       # JPA Repository
│   │   └── service/          # 비즈니스 로직
│   └── src/main/resources/
│       └── application.properties   # DB 및 JWT 설정
│
└── frontend/ (Vue.js)
    ├── src/
    │   ├── api/              # Axios 인스턴스 및 인터셉터
    │   ├── components/       # 공통 컴포넌트 (JobForm, Footer 등)
    │   ├── stores/           # Pinia 상태 관리 (auth, job, todo, history)
    │   ├── types/            # TypeScript 타입 정의
    │   ├── views/            # 페이지 (Home, Login, Jobs, Todos, History 등)
    │   ├── App.vue           # 루트 컴포넌트
    │   └── main.ts           # Vue 앱 인스턴스 생성
    ├── index.html
    └── package.json

```

## 5. 실행 방법
Backend
```Bash

cd backend
./gradlew bootRun

```

Frontend
```Bash

cd frontend
npm install
npm run dev
```