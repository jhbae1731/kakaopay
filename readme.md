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
![image](https://user-images.githubusercontent.com/74831730/99899895-eecab500-2cef-11eb-8b77-ef4f9dd54560.png)

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
### 단위 테스트
- 뿌리기 API
    - 뿌리기 요청건에 대한 고유token을 발급하고 응답값으로 내려줍니다.(`GiveApiTest#test001`)
    
- 받기 API
    - token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.(`ReceiveApiTest#test001`)
    - 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.(`ReceiveApiTest#test002`)
    - 자신이 뿌리기한 건은 자신이 받을 수 없습니다.(`ReceiveApiTest#test003`)
    - 뿌린이가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다.(`ReceiveApiTest#test004`)
    - 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다.(`ReceiveApiTest#test005`)
    
- 조회 API
    - token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재 상태는 다음의 정보를 포함합니다.(`InfoApiTest#test001`)
    - 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.(`InfoApiTest#test002,InfoApiTest#test003`)
    - 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.(`InfoApiTest#test004`)
    
- 단위 테스트 결과

    ![image](https://user-images.githubusercontent.com/74831730/99899315-b7f2a000-2ceb-11eb-92a0-adc56c80e54e.png)
