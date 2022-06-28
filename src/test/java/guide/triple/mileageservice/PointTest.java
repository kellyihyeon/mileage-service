package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {

    private Point point;   // 한 유저의 포인트


    @BeforeEach
    void setUp() {
        point = new Point();
    }

    @Test
    @DisplayName("첨부한 사진을 모두 삭제하면 포인트 1점을 회수한다.")
    @Disabled("restart")
    void 첨부한_사진_모두_삭제() {
        //given
        // 리뷰 작성(글과 사진 모두 있음)
        ReviewEventReqDto dto = ReviewEventReqDtoFixtureBuilder.builder().build();
        point.check(dto);
        int totalPoint = point.getTotalPoint();
        // 리뷰 수정(사진을 삭제함)
        ReviewEventReqDto deleteAllPhotosDto = ReviewEventReqDtoFixtureBuilder.builder()
                .action(ReviewEventAction.MOD)
                .attachedPhotoIds(Collections.emptyList())
                .build();
        //when
        List<RewardsPoint> pointHistories = point.check(deleteAllPhotosDto);    //(1점, 취소, photo)
        int changedTotalPoint = point.getTotalPoint();
        //then
        assertEquals(2, totalPoint);
        assertEquals(1, changedTotalPoint); //
    }

    @Test
    @DisplayName("사진 첨부가 규정에 맞지 않으면 포인트를 부여하지 않는다.")
    void 규정에_맞지않는_사진첨부() {
        ReviewEventReqDto attachedPhotoIsEmpty = ReviewEventReqDtoFixtureBuilder.builder().attachedPhotoIds(Collections.emptyList()).build();

        point.check(attachedPhotoIsEmpty);
        List<RewardsPoint> rewardsPoint = point.getPointHistory();  // REVIEW

        Boolean existedPhotoPoint = rewardsPoint.stream()
                .map(history -> PointDetails.PHOTO.equals(history.getDetails()))
                .findFirst()
                .orElse(false);

        assertFalse(existedPhotoPoint);
    }

    @Test
    @DisplayName("리뷰 내용이 규정에 맞지 않으면 포인트를 부여하지 않는다.")
    void 규정에_맞지않는_리뷰내용() {
        ReviewEventReqDto contentIsNull = ReviewEventReqDtoFixtureBuilder.builder().content(null).build();

        point.check(contentIsNull);
        List<RewardsPoint> rewardsPoint = point.getPointHistory();  // PHOTO

        Boolean existedContentPoint = rewardsPoint.stream()
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
                .build();

        point.check(maybeZeroPoint);

        assertEquals(0, point.getTotalPoint());
    }

    @Test
    @DisplayName("내용과 사진이 있는 경우 보상 점수는 2점이다.")
    void 내용과_사진이_모두_있는_경우() {
        ReviewEventReqDto maybeTwoPoints = ReviewEventReqDtoFixtureBuilder.builder().build();

        point.check(maybeTwoPoints);

        assertEquals(2, point.getTotalPoint());
    }

    @ParameterizedTest
    @MethodSource("dtoGenerator")
    @DisplayName("내용 작성 또는 사진 첨부가 있는 경우 보상 점수는 1점이다.")
    void 내용과_사진_둘_중_하나만_있는_경우(ReviewEventReqDto inputDto) {
        point.check(inputDto);

        assertEquals(1, point.getTotalPoint());
    }


    private static Stream<ReviewEventReqDto> dtoGenerator() {
        return Stream.of(
                ReviewEventReqDtoFixtureBuilder.builder().content(null).build(),
                ReviewEventReqDtoFixtureBuilder.builder().attachedPhotoIds(Collections.emptyList()).build()
        );
    }
}
