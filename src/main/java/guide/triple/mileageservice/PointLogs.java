package guide.triple.mileageservice;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class PointLogs {

    private final List<PointLog> pointLogs;

    public PointLogs(List<PointLog> pointLogs) {
        this.pointLogs = pointLogs;
    }

    public String getPlaceId() {
        return pointLogs.stream().map(PointLog::getPlaceId).findAny().orElse(null);
    }

    public boolean hasLogByContent() {
        return pointLogs.stream().anyMatch(pointLog -> PointDetails.CONTENT.equals(pointLog.getDetails()));
    }

    public boolean isChecked() {
        return pointLogs.stream().map(PointLog::isPointCheck).findFirst().orElse(false);
    }

    public boolean hasLogByPhoto() {
        return pointLogs.stream().anyMatch(pointLog -> PointDetails.PHOTO.equals(pointLog.getDetails()));
    }

}
