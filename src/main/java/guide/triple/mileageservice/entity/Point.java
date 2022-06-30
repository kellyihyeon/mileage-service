package guide.triple.mileageservice.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
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


    private PointLog createPointLog(PointDetails details, String placeId, PointStatus status) {
        return PointLog.builder()
                .point(this)
                .amount(1)
                .status(status)
                .details(details)
                .placeId(placeId)
                .time(LocalDateTime.now())
                .build();
    }


    public PointLog plusPointByFirstReview(ReviewEvent event) {
        plusPoint();
        return createPointLog(PointDetails.FIRST_REVIEW, event.getPlaceId(), PointStatus.ADDED);
    }

    public PointLog minusPointByFirstReview(ReviewEvent event) {
        minusPoint();
        return createPointLog(PointDetails.FIRST_REVIEW, event.getPlaceId(), PointStatus.CANCELED);
    }

    public PointLog plusPointByContent(ReviewEvent event) {
        plusPoint();
        return createPointLog(PointDetails.CONTENT, event.getPlaceId(), PointStatus.ADDED);
    }

    public PointLog minusPointByContent(ReviewEvent event) {
        minusPoint();
        return createPointLog(PointDetails.CONTENT, event.getPlaceId(), PointStatus.CANCELED);
    }

    public PointLog plusPointByPhoto(ReviewEvent event) {
        plusPoint();
        return createPointLog(PointDetails.PHOTO, event.getPlaceId(), PointStatus.ADDED);
    }

    public PointLog minusPointByPhoto(ReviewEvent event) {
        minusPoint();
        return createPointLog(PointDetails.PHOTO, event.getPlaceId(), PointStatus.CANCELED);
    }

    private void plusPoint() {
        totalPoint += 1;
    }

    private void minusPoint() {
        totalPoint -= 1;
    }
}


