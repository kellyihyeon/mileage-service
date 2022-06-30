package guide.triple.mileageservice;

import guide.triple.mileageservice.entity.PointDetails;
import guide.triple.mileageservice.entity.PointStatus;

import java.util.Objects;

public class RewardsPoint {
    // reviewId
    private final Integer amount;
    private final PointStatus status;
    private final PointDetails details;


    public RewardsPoint(int amount, PointStatus status, PointDetails details) {
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
        RewardsPoint rewardsPoint = (RewardsPoint) o;
        return Objects.equals(this.amount, rewardsPoint.amount) && status == rewardsPoint.status && details == rewardsPoint.details;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, status, details);
    }

    @Override
    public String toString() {
        return "RewardsPoint{" +
                "amount=" + amount +
                ", status=" + status +
                ", details=" + details +
                '}';
    }
}
