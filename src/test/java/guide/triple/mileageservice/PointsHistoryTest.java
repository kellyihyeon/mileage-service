package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
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
