package guide.triple.mileageservice.service;

import guide.triple.mileageservice.dto.HistoryDto;
import guide.triple.mileageservice.dto.MyPointHistory;
import guide.triple.mileageservice.entity.Point;
import guide.triple.mileageservice.entity.PointLog;
import guide.triple.mileageservice.repository.PointLogRepository;
import guide.triple.mileageservice.repository.PointRepository;
import guide.triple.mileageservice.common.PointLogIdComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRep;
    private final PointLogRepository pointLogRep;

    public MyPointHistory<HistoryDto> getMyPoint() {
        String currentUserId = "8crf2ew6-25r3-3492-y2w0-2f582458z024";
        Point point = pointRep.findByUserId(currentUserId);
        List<PointLog> logs = pointLogRep.findByPoint(point);

        return new MyPointHistory<>(createHistory(logs), point.getTotalPoint());
    }

    private List<HistoryDto> createHistory(List<PointLog> logs) {
        List<HistoryDto> historyDtos = new ArrayList<>();

        logs.forEach(pointLog ->
                historyDtos.add(
                        new HistoryDto(pointLog.getId(),
                                pointLog.getAmount(),
                                pointLog.getStatus(),
                                pointLog.getDetails(),
                                pointLog.getPlaceId(),
                                pointLog.getTime())));
        historyDtos.sort(new PointLogIdComparator().reversed());
        return historyDtos;
    }

}
