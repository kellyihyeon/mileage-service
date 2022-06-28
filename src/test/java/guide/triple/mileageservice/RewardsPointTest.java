package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class RewardsPointTest {

    private Point point;

    final int CONTENT = 0;
    final int PHOTO = 1;


    @BeforeEach
    void setUp() {
        point = new Point();
    }

    @Test
    @DisplayName("리뷰작성 및 사진첨부 시 (1점, 적립, REVIEW), (1점, 적립, PHOTO) 이력이 남는다.")
    void 리뷰작성_및_사진첨부_이력() {
        ReviewEventReqDto dto = ReviewEventReqDtoFixtureBuilder.builder().build();

        point.check(dto);
        List<RewardsPoint> pointHistories = point.getPointHistory();

        assertEquals(new RewardsPoint(1, PointStatus.ADDED, PointDetails.CONTENT), pointHistories.get(CONTENT));
        assertEquals(new RewardsPoint(1, PointStatus.ADDED, PointDetails.PHOTO), pointHistories.get(PHOTO));
    }

    @ParameterizedTest
    @MethodSource("dtoGenerator")
    @DisplayName("리뷰작성 또는 사진첨부 시 (1점, 적립, REVIEW) 또는 (1점, 적립, PHOTO) 이력이 남는다.")
    void 리뷰작성_또는_사진첨부_이력(ReviewEventReqDto inputDto, int index) {
        point.check(inputDto);
        List<RewardsPoint> pointHistories = point.getPointHistory();

        final int HISTORY_SIZE = 0;
        if (index == 0) {
            assertEquals(new RewardsPoint(1, PointStatus.ADDED, PointDetails.CONTENT), pointHistories.get(HISTORY_SIZE));
        }

        if (index == 1) {
            assertEquals(new RewardsPoint(1, PointStatus.ADDED, PointDetails.PHOTO), pointHistories.get(HISTORY_SIZE));
        }
    }


    private static Stream<Arguments> dtoGenerator() {
        return Stream.of(
                Arguments.of(ReviewEventReqDtoFixtureBuilder.builder().attachedPhotoIds(Collections.emptyList()).build(), 0),
                Arguments.of(ReviewEventReqDtoFixtureBuilder.builder().content(null).build(), 1)
        );
    }

}
