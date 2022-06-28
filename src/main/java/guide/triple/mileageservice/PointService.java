package guide.triple.mileageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRep;
    private final PointLogRepository pointLogRep;



    public void pointEvent(ReviewEvent reviewEvent) {
        Point point = pointRep.findByUserId(reviewEvent.getUserId());

        if (reviewEvent.isActionAdd()) {
            if (reviewEvent.hasContent()) {
                PointLog logWithContent = point.plusPointByContent(reviewEvent);
                pointLogRep.save(logWithContent);
            }

            if (reviewEvent.hasAttachedPhoto()) {
                PointLog logWithPhoto = point.plusPointByPhoto(reviewEvent);
                pointLogRep.save(logWithPhoto);
            }
        }

    }

}

