package guide.triple.mileageservice;

import java.util.Objects;

public class Points {

    private final Integer point;
    private final PointStatus status;
    private final PointDetails details;

    public Points(int point, PointStatus status, PointDetails details) {
        this.point = point;
        this.status = status;
        this.details = details;
    }

    public Integer getPoint() {
        return point;
    }

    public PointStatus getPointStatus() {
        return status;
    }

    public PointDetails getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Points points = (Points) o;
        return Objects.equals(point, points.point) && status == points.status && details == points.details;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, status, details);
    }
}
