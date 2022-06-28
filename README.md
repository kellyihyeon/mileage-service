# 트리플여행자 클럽 마일리지 서비스

## [ 애플리케이션 실행 방법 ]
- 애플리케이션 실행 방법을 제시한다.

<br>

## [ 기능 목록 ]
#### *리뷰 이벤트가 발생하면 포인트를 부여하거나 회수한다.*

- 리뷰가 작성될 때마다 리뷰 작성 이벤트가 발생하고, 아래 API로 이벤트를 전달한다. (Input)
    ``` java
    POST /events
    {
        "type": "REVIEW",
        "action": "ADD", /* "MOD", "DELETE" */
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "content": "좋아요!",
        "attachedPhotoIds": ["e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332"],
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
    }
    ```
<br>

- 리뷰 이벤트가 발생했을 때 보상 점수 계산하기
    ```
    [ 리뷰 작성 보상 점수 ]
    
    내용 점수
      - 1자 이상 텍스트 작성: 1점
      - 1장 이상 사진 첨부: 1점
    
    보너스 점수
      - 특정 장소에 첫 리뷰 작성: 1점
    ```
    - 이벤트의 type 이 REVIEW 이다.
    - 포인트를 적립할 수 있는 다양한 방법 중 리뷰 이벤트를 구현하는 것이 MVP.
    
<br>

- 한 사용자가 한 장소에 리뷰를 여러번 작성할 수 없도록 한다.
    - 유저 id와 place id 의 관계 → place id를 기준으로 데이터를 뽑았을 때 user id가 이미 존재한다면 한 장소에 리뷰를 여러번 달려고 하는 것이므로 막는다.
    - ❓재방문의 경우에는 어떻게 할 것인가?
    
<br>

- POST /events 포인트 부여 API
    - action-ADD   
      '리뷰 작성' 이벤트 발생 -> 리뷰 작성 보상 점수 계산 후 포인트 부여
    - action-MOD   
      '리뷰 수정' 이벤트 발생 -> 포인트 회수 및 유지
    - action-DELETE   
      '리뷰 삭제' 이벤트 발생 -> 포인트 회수
    - SQL 수행 시, full table scan 이 일어나지 않는 인덱스가 필요하다.
    - 리뷰 작성 시점에 첫 리뷰이면 보너스 점수를 부여한다.
    
  <br>
  
- GET /events 포인트 조회 API
    - 포인트 조회 시 포인트 증감 히스토리를 보여준다.

<br>

## [ 문제 분석 및 해결 전략 ]
### 1. Flow
- 리뷰 CRUD API를 담당하는 개발자A님
    ``` java
    POST /reviews
    
    public Result createReview(Dto dto){
            ...
            service.createReview(dto);
    }
    
    Get /reviews
    
    public Result getAllReviews(){
            ...
            service.getAllReviews();
    }
    
    PUT /reviews/{reviewId}
    
    public Result updateReview(){
            ...
            service.updateReview();
    }
    
    DELETE /reviews/{reviewId}
    
    public Result deleteReview(){
            ...
            service.deleteReview();
    }
    ```
    - 리뷰 dto - 별점, 내용, 사진
    - 리뷰 조회 result의 data - 닉네임, 유저레벨, 총 리뷰 갯수, 별점, 내용, 사진, 작성일

<br>

- 포인트 API를 담당하는 나

    ``` java
        POST /events
        
        public ApiResonse<ResponseDto> addToMileagePoints(ReviewEventReqDto dto){
        
        }
        
        GET /events
    ```

    - 포인트 적립 API는 어디에서 호출하는가?
      → 리뷰 작성을 마치면 포인트를 적립한다.  
      프론트에서 `POST /reviews` API를 호출하면서 유저의 리뷰 데이터(별점, 내용, 사진)를 전달한다.  
      서버로부터 정상 응답을 받으면 `POST /events` API를 호출하면서 이벤트 데이터를 전달한다.
      
    - 전달된 이벤트 데이터를 토대로 포인트를 계산한 후 적립한다.
      
    - 포인트 조회 API를 호출하면 포인트 증감 히스토리를 보여준다.  
<br>

### 2. action 데이터에 따른 상이한 접근
- action이 MOD, DELETE인 경우는 리뷰 작성(ADD)에 대한 포인트 적립이 이미 이루어진 후이므로 API 요청마다 포인트 중복 적립이 일어나지 않도록 한다.

- ADD 일 때: 사용자가 리뷰를 작성한다.
    - place id를 기준으로 데이터 조회 → user id를 기준으로 중복 리뷰 체크
    - content 조건 충족 여부 및 점수 부여
    - photo 첨부 여부 및 점수 부여
    - 보너스 점수 획득 여부 및 점수 부여
        - place id를 기준으로 데이터를 뽑았을 때, review id 존재 여부
        - 첫 리뷰의 시점: 사용자 입장. → 동시성 문제 발생 가능성.
            - 사용자 A-리뷰 삭제, 사용자 B-리뷰 작성이 동시에 일어난다면 보너스 점수를 계산하는 데 이슈가 생길 수 있음.
            - POST /events api 호출 → 해당 place id를 기준으로 review id 존재 여부를 확인 (a의 리뷰 존재o) → a가 리뷰를 삭제 → 해당 place id에 review 가 존재하지 않음 → b의 데이터를 리뷰 데이터베이스에 저장 → b는 보너스 점수를 획득하지 못함.
            - 위 경우와 a의 리뷰 작성 → b의 리뷰 작성 → a의 리뷰 삭제가 순차적으로 발생한 경우 어떻게 구별할 것인가?
            
<br>

- MOD 일 때 : 사용자가 리뷰를 수정한다.
  - ADD로 보상 점수를 받았을 때
    - 보상 영역(내용, 사진)과 보상 점수에 대한 데이터가 필요하다.
    - (내용, 1), (사진, 1) ...
  
  - ADD로 보상 점수를 받지않았을 때
    - (내용, 0), (사진, 0)
    - **내용이 길어지니 노션에서 정리하고 옮길 것**
    
---
---

  - 기존의 보상 점수에 증감을 한다.
  - 기존 보상 점수를 유지한다.
  - 보상 점수는 임계영역이고 동시성 문제에서 자유로울 수 없다. 동기화 처리를 해야 한다.
    
    - content 혹은 photo 를 수정(정보 더하기 혹은 줄이기) 한다.
        - 무 → 유의 경우: 점수 추가 및 데이터 변경
        - 유 → 유의 경우: 데이터 변경
    - content 혹은 photo 를 전체 삭제한다.
        - 유 → 무의 경우: 점수 삭감 및 데이터 변경

            
<br>

- DELETE 일 때 : 사용자가 리뷰를 삭제한다.
    - 점수 삭감 및 데이터 삭제

<br>

### 3. 포인트 증감 히스토리 남기기

```java
[ Points ]

Long id;
Long totalAmount;
Long amount;
String userId;
String status;  // 적립, 회수, 사용
String reviewId;
List<Points> history; // 포인트 히스토리
```
- Users 는 rewardsPoint id 를 가지고 있다.
- POST /events API 호출 시 (포인트 적립 API)
    - ADD, MOD, DELETE 의 경우 비즈니스 로직 과정 중 점수 계산이 이루어짐.
    - A의 리뷰 작성 결과 2점을 획득했을 때, 포인트 객체는?
        - 객체를 생성하고 amount, totalAmoutn, userId, status, reviewId 에 데이터를 매핑한다.
        - history에 생성된 Points를 add 해준다.
- GET /events API 호출 시 (포인트 조회 API)
    - Points 의 history를 보여준다.
    - 페이징 처리를 어떻게 할 것인지도 생각한다.

<br>
<br>

## [ 데이터베이스 ]
    



