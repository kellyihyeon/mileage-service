# 트리플여행자 클럽 마일리지 서비스

## **서비스 소개**
- 사용자가 여행 장소에 대한 리뷰를 작성하면 포인트를 적립해준다.
- 전체/개인에 대한 포인트 적립 히스토리를 확인할 수 있다.
- 개인별 누적 포인트를 관리할 수 있다.

<br>

## **기능 분석**
- 한 사용자는 장소마다 리뷰를 1개만 작성할 수 있다.

- 리뷰는 수정 또는 삭제할 수 있다.

- 리뷰 작성 보상 점수 규정대로 포인트를 계산한다.
  ```text
  - 내용 점수
    1자 이상 텍스트 작성: 1점
    1장 이상 사진 첨부: 1점

  - 보너스 점수
    특정 장소에 첫 리뷰 작성: 1점

  ```

- 포인트 증감이 있을 때마다 이력이 남아야한다.

- 사용자마다 현 시점의 포인트 총점을 조회하거나 계산할 수 있어야 한다.

- 포인트 적립 API 구현에 필요한 SQL 수행 시, 전체 테이블 스캔이 일어나지 않는 인덱스가 필요하다.

- 리뷰를 작성했다가 삭제하면 해당 리뷰로 적립한 리뷰 내용 포인트와 보너스 포인트는 회수한다.

- 리뷰를 수정하면 수정한 내용에 맞는 리뷰 내용 포인트를 계산하여 적립하거나 회수한다.
  ```text
    - 글만 작성한 리뷰에 사진 추가: 1포인트 적립
    - 글과 사진이 있는 리뷰에서 사진 모두 삭제: 1포인트 회수
  ```

- 사용자 입장에서 본 ‘첫 리뷰’일 때 보너스 포인트를 적립한다.
  ```java
    - 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하고, 삭제된 이후 사용자 B가 리뷰를 남기면 사용자 B에게 보너스 포인트 적립

    - 어떤 장소에 사용자 A가 리뷰를 남겼다가 삭제하는데, 삭제되기 이전에 사용자 B가 리뷰를 남기면 사용자 B에게 보너스 포인트 적립 X

  ```

<br> 
  
## **DB 스키마 설계**
```java
포인트 (포인트ID(PK), 유저ID, 총 포인트)
포인트로그 (포인트로그ID(PK), 포인트ID(FK), 포인트점수(FK), 포인트상태, 포인트설명, 장소ID, 시간)
```
```java
CREATE TABLE Point(
	id BIGINT,
	userid VARCHAR(255),
	total_point INTEGER,
	CONSTRAINT point_id PRIMARY KEY(id)
);

CREATE TABLE Point_Log(
  id BIGINT,
  amount INTEGER,
  details VARCHAR(255),
  place_id VARCHAR(255),
  status VARCHAR(255),
  time TIMESTAMP,
  point_id BIGINT,
  PRIMARY KEY(id),
  FOREIGN KEY(point_id) REFERENCES Point(id)
);
```
```text
INSERT INTO POINT(USER_ID) VALUES ('3ede0ef2-92b7-4817-a5f3-0c575361f745');
INSERT INTO POINT(USER_ID) VALUES ('5crf8ew7-46r3-8243-y3w9-3f376543s932');
INSERT INTO POINT(USER_ID) VALUES ('8crf2ew6-25r3-3492-y2w0-2f582458z024');
```
- data.sql 파일에 유저 3명의 아이디를 INSERT 해놓았다.

<br>

## **애플리케이션 실행 방법**
- 현재 접속한 유저의 아이디는 "8crf2ew6-25r3-3492-y2w0-2f582458z024"로 설정을 해놓았다.

- MileageServiceApplication 파일을 실행한다.
- postman으로 api 요청을 한다.

- 포인트 적립 API - POST /events 
  - ADD 이벤트 요청하기
    ```json
    {
        "type": "REVIEW",
        "action": "ADD",
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "content": "파리에 다녀오니 너무 좋았다.",
        "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8","afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
        "userId": "8crf2ew6-25r3-3492-y2w0-2f582458z024",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
    }
    ```
  - MOD 이벤트 요청하기 (리뷰 내용 삭제)
    ```json
    {
        "type": "REVIEW",
        "action": "MOD",
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "content": "",
        "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
        "userId": "8crf2ew6-25r3-3492-y2w0-2f582458z024",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
    }
    ```
  - MOD 이벤트 요청하기 (리뷰 사진 삭제)
    ```json
    {
        "type": "REVIEW",
        "action": "MOD",
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "content": "파리에 다녀오니 너무 좋았다.",
        "attachedPhotoIds": [],
        "userId": "8crf2ew6-25r3-3492-y2w0-2f582458z024",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
    }
    ```
  - DELETE 이벤트 요청하기 (리뷰 삭제)
    ```json
    {
        "type": "REVIEW",
        "action": "DELETE",
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "content": "파리에 다녀오니 너무 좋았다.",
        "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8","afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
        "userId": "8crf2ew6-25r3-3492-y2w0-2f582458z024",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
    }
    ```

- 내포인트 조회 API: GET /events
  - 총 포인트 점수 및 포인트 내역 조회  
  ![getMyPoint](https://user-images.githubusercontent.com/73330352/176680801-c855a7be-c3fa-473d-a62f-1e244a04847e.png)

<br>

## **해결 전략**
### **[ 보너스 포인트 처리 ]**
- 첫 리뷰 조건
    ```java
    포인트 로그들
    
    로그1  포인트id3  1점  ADDED     FIRST_REVIEW   괌id  2022-06-20
    로그2  포인트id3  1점  CANCELED  FIRST_REVIEW   괌id  2022-06-30
    ```
    - 해당 장소에대한 포인트로그가 하나도 없다면 첫 리뷰이다.
    - 포인트로그가 있지만  FIRST_REVIEW - CANCELED 상태라면 첫 리뷰이다.
    <br>

- 첫 리뷰 보상
    - 첫 리뷰 등록만 하면 +1
    - 첫 리뷰이면서 컨텐트, 포토 조건을 충족하면 각 +1 (최대 3포인트 획득)

- 첫 리뷰 작성 시 보너스 포인트를 받았다면, 리뷰 삭제 시 보너스 포인트도 회수한다.
    ```java
    포인트 로그들 
    																				
    로그1  포인트id3  1점  ADDED     FIRST_REVIEW   괌id  2022-06-20
    로그2  포인트id3  1점  ADDED     PHOTO          괌id  2022-06-20
    
    로그3  포인트id3  1점  CANCELED  FIRST_REVIEW   괌id  2022-06-30
    로그4  포인트id3  1점  CANCELED  PHOTO          괌id  2022-06-30
    ```
  <br>

- action_ADD 일 때
    - 검사해야할 대상은 특정 장소가 가진 포인트 로그들이다.
    - 예를들면 괌 장소에 리뷰를 쓴 모든 사람들의 로그들을 대상으로 검사한다.

- action_DELETE 일 때
    - 검사해야할 대상은 특정 장소에 && 특정 유저가 가진 로그들이다.
    - 예를들면 괌 장소에 리뷰를 쓴 특정 유저의 로그들을 대상으로 검사한다.

<br>

### **[ action 데이터에 따른 상이한 접근 ]**
- **한 사용자는 장소마다 리뷰를 1개만 작성할 수 있다.**
    
    ```java
    포인트 로그들 (Place괌)          
    
    1  pointId3  1점  ADDED  CONTENT 괌id  ADD
    2  pointId3  1점  ADDED  PHOTO   괌id  ADD
    ```
    
    - action ADD 이벤트 발생
        - CONTENT 혹은 PHOTO의 트랜잭션 마지막 상태가 ADDED라면 리뷰를 작성하지 못한다.
        - CANCELED이라면 특정 장소 리뷰가 삭제됐으므로 리뷰 작성이 가능하다.

    - **재방문**의 경우에는 어떻게 할 것인가?
        - *`PointLog`* 필드에 주문관련 id 추가
        - *`PointLog`* 의 주문 id와 발생한 이벤트의 주문 id 비교
        - 다르다면 action ADD 이벤트의 데이터 내용에 따라 포인트 보상

- **ADD 일 때 (리뷰 작성)**
    - 중복 작성인지 검사
    - 첫 리뷰인지 검사
    - 컨텐트 조건을 충족하면 포인트+
    - 포토 조건을 충족하면 포인트+

- **MOD 일 때 (리뷰 수정)**
    ```java
    포인트 로그들 (Place괌)          
    
    1  pointId3  1점  ADDED     CONTENT  괌id  ADD
    2  pointId3  1점  ADDED     PHOTO    괌id  ADD
    3  pointId3  1점  CANCELED  PHOTO    괌id  MOD- 리뷰 수정(포토 삭제)
    4  pointId3  1점  CANCELED  CONTENT  괌id  DELETE- 리뷰 삭제
    ```
    
    - CONTENT 로그 기록
        - status가 ADDED: 컨텐트 조건을 충족하지 않으면 포인트-
        - status가 CANCELED: 컨텐트 조건을 충족하면 포인트+

    - PHOTO 로그 기록
        - status가 ADDED: 포토 조건을 충족하지 않으면 포인트-
        - status가 CANCELED: 포토 조건을 충족하면 포인트+

- **DELETE 일 때 (리뷰 삭제)**
    - FIRST REVIEW 로그 기록
        - status가 ADDED: 포인트를 받은 리뷰를 삭제하므로 포인트-
    - CONTENT 로그 기록
        - status가 ADDED: 포인트-
    - PHOTO 로그 기록
        - status가 ADDED: 포인트-
