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

    // check 메서드

    public Point actionCheck(ReviewEvent reviewEvent) { //checkByAction

        //1.dto action : DEL (리뷰_작성) -> 포인트 취소 연산
        if (reviewEvent.isActionDelete()) {

        }

        //1.dto action : MOD (리뷰_수정) -> 포인트 적립 or 취소 연산
        if (reviewEvent.isActionMod()) {

        }

        if (reviewEvent.isActionAdd()) {
            if (reviewEvent.hasContent()) {
                addPoint(PointDetails.REVIEW);
            }

            if (reviewEvent.hasAttachedPhoto()) {
                addPoint(PointDetails.PHOTO);
            }
        }

        return null;
    }

    private void addPoint(PointDetails details) {
        PointLog logWithContent = PointLog.builder()
                .point(this)
                .amount(1)
                .status(PointStatus.ADDED)
                .details(details)
                .build();
    }
}


