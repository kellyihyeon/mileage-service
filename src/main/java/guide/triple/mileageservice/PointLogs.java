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
}
