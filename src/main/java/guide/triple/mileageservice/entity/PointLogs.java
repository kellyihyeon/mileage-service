package guide.triple.mileageservice.entity;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PointLogs {

    private final List<PointLog> pointLogs;


    public PointLogs(List<PointLog> pointLogs) {
        this.pointLogs = pointLogs;
    }



    public PointStatus getPointTxStatus(PointDetails details) {
        PointStatus status = PointStatus.NOTHING_DEFAULT;

        for (PointLog pointLog : pointLogs) {
            if (details.equals(pointLog.getDetails())) {
                status = pointLog.getStatus();
            }
        }
        return status;
    }

    public boolean pointTxStatusIsAdded(PointDetails details) {
        return PointStatus.ADDED.equals(getPointTxStatus(details));
    }

    public List<PointLog> getLogsByFirstReview() {
        return pointLogs.stream().filter(pointLog -> PointDetails.FIRST_REVIEW.equals(pointLog.getDetails())).collect(Collectors.toList());
    }

}
