package guide.triple.mileageservice;

import lombok.Builder;
import lombok.NoArgsConstructor;
import javax.persistence.*;

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

    @Builder
    public PointLog(Point point, Integer amount, PointStatus status, PointDetails details) {
        this.point = point;
        this.amount = amount;
        this.status = status;
        this.details = details;
    }
}
