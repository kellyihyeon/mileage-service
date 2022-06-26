package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class ReviewRewardsCalculatorTest {
    private static String type;
    private static ReviewEventAction action;
    private static String reviewId;
    private static String content;
    private static List<String> attachedPhotoIds;
    private static String userId;
    private static String placeId;

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
        assertEquals(0, rewards);
    }

    @Test
    @DisplayName("내용과 사진이 있는 경우 보상 점수는 2점이다.")
    void 내용과_사진이_모두_있는_경우() {
        //given
        ReviewEventReqDto eventDto = new ReviewEventReqDto(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
        //when
        ReviewRewardsCalculator rewardsCalculator = new ReviewRewardsCalculator();
        rewardsCalculator.calculate(eventDto);
        //then
        assertEquals(2, rewardsCalculator.getRewards());
    }

    @ParameterizedTest
    @MethodSource("dtoGenerator")
    @DisplayName("내용 작성 또는 사진 첨부가 있는 경우 보상 점수는 1점이다.")
    void 내용과_사진_둘_중_하나만_있는_경우(ReviewEventReqDto inputDto) {
        //when
        ReviewRewardsCalculator rewardsCalculator = new ReviewRewardsCalculator();
        rewardsCalculator.calculate(inputDto);
        //then
        assertEquals(1, rewardsCalculator.getRewards());
    }


    private static Stream<ReviewEventReqDto> dtoGenerator() {
        return Stream.of(
                new ReviewEventReqDto(type, action, reviewId, null, Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8"), userId, placeId),
                new ReviewEventReqDto(type, action, reviewId, "good", Collections.emptyList(), userId, placeId)
        );
    }
}
