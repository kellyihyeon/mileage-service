package guide.triple.mileageservice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class PointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    private Integer amount;

    @Enumerated(value = EnumType.STRING)
    private PointStatus status;

    @Enumerated(value = EnumType.STRING)
    private PointDetails details;

    private String placeId;


    @Builder
    public PointLog(Point point, Integer amount, PointStatus status, PointDetails details, String placeId) {
        this.point = point;
        this.amount = amount;
        this.status = status;
        this.details = details;
        this.placeId = placeId;
    }

    public boolean statusIsAdded() {
        return PointStatus.ADDED.equals(this.status);
    }
}
