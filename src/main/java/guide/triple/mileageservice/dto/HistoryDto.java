package guide.triple.mileageservice.dto;

import guide.triple.mileageservice.entity.PointDetails;
import guide.triple.mileageservice.entity.PointLog;
import guide.triple.mileageservice.entity.PointStatus;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class HistoryDto {

    private final Long pointLogId;
    private final Integer amount;
    private final PointStatus status;
    private final PointDetails details;
    private final String placeId;
    private final LocalDateTime now;

    public HistoryDto(Long pointLogId, Integer amount, PointStatus status,
                      PointDetails details, String placeId, LocalDateTime now) {
        this.pointLogId = pointLogId;
        this.amount = amount;
        this.status = status;
        this.details = details;
        this.placeId = placeId;
        this.now = now;
    }

    public static HistoryDto of(PointLog pointLog) {
        return new HistoryDto(
                                pointLog.getId(),
                                pointLog.getAmount(),
                                pointLog.getStatus(),
                                pointLog.getDetails(),
                                pointLog.getPlaceId(),
                                pointLog.getTime());
    }

}
