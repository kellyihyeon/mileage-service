package guide.triple.mileageservice;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PointHistoryTest {

    private List<Point> points;

    @BeforeEach
    void setUp() {
        ReviewEventReqDto dto = ReviewEventReqDtoFixtureBuilder.builder().build();
        ReviewPointEvent calc = new ReviewPointEvent();
        points = calc.check(dto);
    }

    @Test
    @DisplayName("리뷰 작성 및 사진 첨부 시 각 (1포인트, PLUS) 이력이 남는다.")
    void 리뷰작성_및_사진첨부_이력() {
        Point contentPoint = new Point(1, PointStatus.ADDED, PointDetails.REVIEW);
        Point photoPoint = new Point(1, PointStatus.ADDED, PointDetails.PHOTO);

        int CONTENT = 0;
        int PHOTO = 1;

        assertEquals(contentPoint, this.points.get(CONTENT));
        assertEquals(photoPoint, this.points.get(PHOTO));
    }

    @Test
    @DisplayName("리뷰 작성시 (1포인트, PLUS) 이력이 남는다.")
     void 리뷰작성_이력() {
        Point point = points.get(0);

        assertEquals(1, point.getAmount());
        assertEquals(PointStatus.ADDED, point.getPointStatus());
    }

}
