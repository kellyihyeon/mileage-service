package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PointsHistoryTest {

    private List<Points> points;

    @BeforeEach
    void setUp() {
        ReviewEventReqDto dto = ReviewEventReqDtoFixtureBuilder.builder().build();
        ReviewPointsCalculator calc = new ReviewPointsCalculator();
        points = calc.check(dto);
    }

    @Test
    @DisplayName("사진 첨부가 규정에 맞지 않으면 포인트를 부여하지 않는다.")
    void 규정에_맞지않는_사진첨부() {
        ReviewEventReqDto dto = ReviewEventReqDtoFixtureBuilder.builder().attachedPhotoIds(Collections.emptyList()).build();
        ReviewPointsCalculator calc = new ReviewPointsCalculator();
        points = calc.check(dto);

        Points pointWithoutPhoto = this.points.get(0);

        assertNotEquals(PointDetails.PHOTO, pointWithoutPhoto.getDetails());
    }

    @Test
    @DisplayName("리뷰 내용이 규정에 맞지 않으면 포인트를 부여하지 않는다.")
    void 규정에_맞지않는_리뷰내용() {
        ReviewEventReqDto dto = ReviewEventReqDtoFixtureBuilder.builder().content(null).build();
        ReviewPointsCalculator calc = new ReviewPointsCalculator();
        points = calc.check(dto);

        Points pointWithoutContent = this.points.get(0);

        assertNotEquals(PointDetails.REVIEW, pointWithoutContent.getDetails());
    }

    @Test
    @DisplayName("리뷰 작성 및 사진 첨부 시 각 (1포인트, PLUS) 이력이 남는다.")
    void 리뷰작성_및_사진첨부_이력() {
        Points contentPoint = new Points(1, PointStatus.PLUS, PointDetails.REVIEW);
        Points photoPoint = new Points(1, PointStatus.PLUS, PointDetails.PHOTO);

        int CONTENT = 0;
        int PHOTO = 1;

        assertEquals(contentPoint, this.points.get(CONTENT));
        assertEquals(photoPoint, this.points.get(PHOTO));
    }

    @Test
    @DisplayName("리뷰 작성시 (1포인트, PLUS) 이력이 남는다.")
     void 리뷰작성_이력() {
        Points point = points.get(0);

        assertEquals(1, point.getPoint());
        assertEquals(PointStatus.PLUS, point.getPointStatus());
    }

}
