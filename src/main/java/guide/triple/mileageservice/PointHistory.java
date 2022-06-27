package guide.triple.mileageservice;

import java.util.Objects;

public class PointHistory {

    private final Integer amount;
    private final PointStatus status;
    private final PointDetails details;


    public PointHistory(int amount, PointStatus status, PointDetails details) {
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
        PointHistory pointHistory = (PointHistory) o;
        return Objects.equals(this.amount, pointHistory.amount) && status == pointHistory.status && details == pointHistory.details;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, status, details);
    }

    @Override
    public String toString() {
        return "PointHistory{" +
                "amount=" + amount +
                ", status=" + status +
                ", details=" + details +
                '}';
    }
}
