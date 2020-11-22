# 핵심문제해결 전략

---

### Lean Software Development & Prototype Model

- Eliminate Waste : 불필요한 코드나 기능, 불분명한 요구사항 제거
- Build Integrity In : 단위테스트
- Amplify Learning : 학습
- Deliver as Fast as Possible : 빠른 인도

### 개발 환경
- 기본 환경
    - IDE: Eclipse Neon
    - OS: Window 10
    - GIT
- Server
    - Java8
    - Spring Boot
    - JPA
    - QueryDSL
    - H2
    - lombok
    - Maven
    - Junit5
---

### Data Flow

![image](https://user-images.githubusercontent.com/74831730/99898913-993eda00-2ce8-11eb-8ce2-67677e77a914.png)

---
### Table
![image](https://user-images.githubusercontent.com/74831730/99899003-5b8e8100-2ce9-11eb-8276-62fbd24fcf60.png)

---
### API 명세
- 공통사항
해더

| 항목         | 값 (예)          | 설명            |
| ------------ | ---------------- | --------------- |
| Content-Type | application/json | `JSON` 으로 요청 |
| X-ROOM-ID    | A                | 대화방 식별값   |
| X-USER-ID    | 12               | 사용자 식별값   |
