package guide.triple.mileageservice;

import java.util.Objects;

public class Point {    // PointHistory

    private final Integer amount;
    private final PointStatus status;
    private final PointDetails details;


    public Point(int amount, PointStatus status, PointDetails details) {
        this.amount = amount;
        this.status = status;
        this.details = details;
    }

    public Integer getAmount() {
        return amount;
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
        Point point = (Point) o;
        return Objects.equals(this.amount, point.amount) && status == point.status && details == point.details;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, status, details);
    }

    @Override
    public String toString() {
        return "Point{" +
                "amount=" + amount +
                ", status=" + status +
                ", details=" + details +
                '}';
    }
}
