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
    - Header

    | 항목         | 값 (예)          | 설명            |
    | ------------ | ---------------- | --------------- |
    | Content-Type | application/json | `JSON` 으로 요청 |
    | X-ROOM-ID    | room1            | 대화방 식별값   |
    | X-USER-ID    | 123              | 사용자 식별값   |

     - Output
     
    | 이름       |  타입  | 필수 | 설명                                                         |
    | ---------- | :----: | :---: | ------------------------------------------------------------ |
    | code      | String |  ○   | 상태코드                                           |
    | msg      | String |  ○   | 상태메세지                                          |
    
- 뿌리기 API(`POST` /kakaopay/api)

    - Input

    | 이름       |  타입  | 필수 | 설명                                                         |
    | ---------- | :----: | :---: | ------------------------------------------------------------ |
    | give_money     | int |  ○   | 뿌릴 금액                                           |
    | people_count      | int  |  ○   | 뿌릴 인원                                           |
    
    - Output
    
    | 이름       |  타입  | 필수 | 설명                                                         |
    | ---------- | :----: | :---: | ------------------------------------------------------------ |
    | data      | String |  ○   | 토큰                                           |
   
    
    -Print
    ```json
    {
      "code": "C001",
      "msg": "SUCCESS",
      "data": "pQv"
    }
    ```
- 받기 API(`PUT` /kakaopay/api/{token})
   
    - Output
    
    | 이름       |  타입  | 필수 | 설명                                                         |
    | ---------- | :----: | :---: | ------------------------------------------------------------ |
    | data      | int |  ○   | 받은 금액                                           |
    
    - Print
    ```json
    {
      "code": "C001",
      "msg": "SUCCESS",
      "data": 2000
    }
    ```

- 조회 API(`GET` /kakaopay/api/{token})
   
    - Output
    
    | 이름 |  타입  | 필수 | 설명        |
    | ---- | :----: | :---: | ----------- |
    | data.give_date | string | ○ | 뿌린 시각 |
    | data.complete_money | int | ○ | 받기 완료된 금액 |
    | data.give_money | int | ○ | 뿌린 금액 |
    | data.receive_info[].receive_money | int | ○ | 받은 금액 |
    | data.receive_info[].receive_user_id | int | ○ | 받은 사용자 식별값 |

    ```json
    {
      "code": "C001",
      "msg": "SUCCESS",
      "data": {
        "receive_info": [
            {
                "receive_money": 885,
                "receive_user_id": 3
            }
        ],
        "give_date": "2020-11-21T18:04:43.377",
        "complete_money": 885,
        "give_money": 10000
      }
    }
    ```
---
###
