package guide.triple.mileageservice;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PointLogs {

    private final List<PointLog> pointLogs;


    public PointLogs(List<PointLog> pointLogs) {
        this.pointLogs = pointLogs;
    }



    public boolean hasLogByContent() {
        return pointLogs.stream().anyMatch(pointLog -> PointDetails.CONTENT.equals(pointLog.getDetails()));
    }


    public boolean hasLogByPhoto() {
        return pointLogs.stream().anyMatch(pointLog -> PointDetails.PHOTO.equals(pointLog.getDetails()));
    }


    public List<PointLog> getLogsByContent() {
        return pointLogs.stream().filter(pointLog -> PointDetails.CONTENT.equals(pointLog.getDetails())).collect(Collectors.toList());
    }

    public List<PointLog> getLogsByPhoto() {
        return pointLogs.stream().filter(pointLog -> PointDetails.PHOTO.equals(pointLog.getDetails())).collect(Collectors.toList());
    }

    public PointStatus getPointTxStatus() {
        PointStatus status = null;

        for (PointLog pointLog : pointLogs) {
            if (PointStatus.ADDED.equals(pointLog.getStatus())) {
                status = PointStatus.ADDED;
            } else if (PointStatus.CANCELED.equals(pointLog.getStatus())) {
                status = PointStatus.CANCELED;
            }
        }
        return status;
    }

    public PointStatus getPointTxStatus(PointDetails details) { //FIRST_REVIEW, CONTENT, PHOTO
        PointStatus status = PointStatus.NOTHING_DEFAULT;

        for (PointLog pointLog : pointLogs) {
            if (details.equals(pointLog.getDetails())) {
                status = pointLog.getStatus();
            }
        }
        return status;
    }

    public boolean pointTxStatusIsAdded() { //
        return PointStatus.ADDED.equals(getPointTxStatus());
    }

    public boolean pointTxStatusIsAdded(PointDetails details) {
        return PointStatus.ADDED.equals(getPointTxStatus(details));

    }

    public List<PointLog> getLogsByFirstReview() {
        return pointLogs.stream().filter(pointLog -> PointDetails.FIRST_REVIEW.equals(pointLog.getDetails())).collect(Collectors.toList());
    }

    public Long getTotalPoint() {
        return pointLogs.stream().map(pointLog -> Math.max(Integer.MIN_VALUE, pointLog.getId())).findFirst().orElse(null);
    }

    public boolean hasLogByFirstReview() {
        return pointLogs.stream().anyMatch(pointLog -> PointDetails.FIRST_REVIEW.equals(pointLog.getDetails()));
    }
}
