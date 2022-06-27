package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class PointCalculatorTest {

    private ReviewPointEvent reviewPointEvent;


    @BeforeEach
    void setUp() {
        reviewPointEvent = new ReviewPointEvent(); // 한 유저의 포인트
    }

    @Test
    @DisplayName("사진 첨부가 규정에 맞지 않으면 포인트를 부여하지 않는다.")
    void 규정에_맞지않는_사진첨부() {
        ReviewEventReqDto attachedPhotoIsEmpty = ReviewEventReqDtoFixtureBuilder.builder().attachedPhotoIds(Collections.emptyList()).build();

        reviewPointEvent.check(attachedPhotoIsEmpty);
        List<Point> pointHistory = reviewPointEvent.getPointHistory();  // REVIEW

        Boolean existedPhotoPoint = pointHistory.stream()
                .map(history -> PointDetails.PHOTO.equals(history.getDetails()))
                .findFirst()
                .orElse(false);

        assertFalse(existedPhotoPoint);
    }

    @Test
    @DisplayName("리뷰 내용이 규정에 맞지 않으면 포인트를 부여하지 않는다.")
    void 규정에_맞지않는_리뷰내용() {
        ReviewEventReqDto contentIsNull = ReviewEventReqDtoFixtureBuilder.builder().content(null).build();

        reviewPointEvent.check(contentIsNull);
        List<Point> pointHistory = reviewPointEvent.getPointHistory();  // PHOTO

        Boolean existedContentPoint = pointHistory.stream()
                .map(history -> PointDetails.REVIEW.equals(history.getDetails()))
                .findFirst()
                .orElse(false);

        assertFalse(existedContentPoint);
    }

    @Test
    @DisplayName("내용도 사진도 없는 경우 보상 점수는 0점이다.")
    void nothing() {
        ReviewEventReqDto maybeZeroPoint = ReviewEventReqDtoFixtureBuilder.builder().
                content(null).
                attachedPhotoIds(Collections.emptyList())
                .build();    // 괌 리뷰

        reviewPointEvent.check(maybeZeroPoint);

        assertEquals(0, reviewPointEvent.getTotalPoint());
    }

    @Test
    @DisplayName("내용과 사진이 있는 경우 보상 점수는 2점이다.")
    void 내용과_사진이_모두_있는_경우() {
        ReviewEventReqDto maybeTwoPoints = ReviewEventReqDtoFixtureBuilder.builder().build();

        reviewPointEvent.check(maybeTwoPoints);

        assertEquals(2, reviewPointEvent.getTotalPoint());
    }

    @ParameterizedTest
    @MethodSource("dtoGenerator")
    @DisplayName("내용 작성 또는 사진 첨부가 있는 경우 보상 점수는 1점이다.")
    void 내용과_사진_둘_중_하나만_있는_경우(ReviewEventReqDto inputDto) {
        reviewPointEvent.check(inputDto);

        assertEquals(1, reviewPointEvent.getTotalPoint());
    }


    private static Stream<ReviewEventReqDto> dtoGenerator() {
        return Stream.of(
                ReviewEventReqDtoFixtureBuilder.builder().content(null).build(),
                ReviewEventReqDtoFixtureBuilder.builder().attachedPhotoIds(Collections.emptyList()).build()
        );
    }
}
