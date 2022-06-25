package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReviewRewardsCalculatorTest {
    private String type;
    private ReviewEventAction action;
    private String reviewId;
    private String content;
    private List<String> attachedPhotoIds;
    private String userId;
    private String placeId;

    @BeforeEach
    void setUp() {
        type = "REVIEW";
        action = ReviewEventAction.ADD;
        reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772";
        content = "좋아요 !";
        attachedPhotoIds = Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
        userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";
    }

    @Test
    @DisplayName("내용도 사진도 없는 경우 보상 점수는 0점이다.")
    void nothing() {
        //given
        ReviewEventReqDto eventDto = new ReviewEventReqDto(type, action, reviewId, null, Collections.emptyList(), userId, placeId);
        //when
        String reviewEvent = eventDto.getType();
        ReviewRewardsCalculator rewardsCalculator = new ReviewRewardsCalculator();
        rewardsCalculator.calculate(eventDto);
        int rewards = rewardsCalculator.getRewards();
        //then
        Assertions.assertEquals(0, rewards);
    }
}
