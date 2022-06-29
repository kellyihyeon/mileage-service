package guide.triple.mileageservice;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.util.List;

@Slf4j
@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @OneToMany(mappedBy = "point")
    private List<PointLog> history;

    @ColumnDefault("0")
    private int totalPoint;



    public PointLog plusPointByPhoto(ReviewEvent event) {
        plusPoint();
        return createPointLog(PointDetails.PHOTO, event.getPlaceId(), PointStatus.ADDED);
    }

    public PointLog plusPointByContent(ReviewEvent event) {
        plusPoint();
        return createPointLog(PointDetails.CONTENT, event.getPlaceId(), PointStatus.ADDED);
    }

    private void plusPoint() {
        totalPoint += 1;
    }

    private PointLog createPointLog(PointDetails details, String placeId, PointStatus status) {
        return PointLog.builder()
                .point(this)
                .amount(1)
                .status(status)
                .details(details)
                .placeId(placeId)
                .build();
    }

    public PointLog minusPointByContent(ReviewEvent event) {
        minusPoint();
        return createPointLog(PointDetails.CONTENT, event.getPlaceId(), PointStatus.CANCELED);
    }

    private void minusPoint() {
        totalPoint -= 1;
    }
}


