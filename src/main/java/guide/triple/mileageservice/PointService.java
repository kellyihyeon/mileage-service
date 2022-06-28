package guide.triple.mileageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {         //PointEventService

    private final PointRepository pointRep;
    private final PointLogRepository pointLogRep;


    /**
     * todo
     * 한 장소에 하나의 리뷰만 쓸 수 있는 로직 추가 - ADD 두 번 요청 시 Exception !
     */
    public void pointEvent(ReviewEvent reviewEvent) {
        Point point = pointRep.findByUserId(reviewEvent.getUserId());

        if (reviewEvent.isActionMod()) {
            List<PointLog> pointLogs = pointLogRep.findByPlaceId(reviewEvent.getPlaceId());
            PointLogs pointTransactionLogs = new PointLogs(pointLogs);

            if (!pointTransactionLogs.hasLogByContent()) {
                processContentPoint(point, reviewEvent);
            }else {
                if (!pointTransactionLogs.isChecked()) {
                    processContentPoint(point, reviewEvent);
                }
            }

            if (!pointTransactionLogs.hasLogByPhoto()) {
                processPhotoPoint(point, reviewEvent);
            } else {
                if (!pointTransactionLogs.isChecked()) {
                    processPhotoPoint(point, reviewEvent);
                }
            }

        }

        if (reviewEvent.isActionAdd()) {
            processContentPoint(point, reviewEvent);
            processPhotoPoint(point, reviewEvent);
        }

    }

    private void processPhotoPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasAttachedPhoto()) {
            PointLog logWithPhoto = point.plusPointByPhoto(reviewEvent);
            pointLogRep.save(logWithPhoto);
        }
    }

    private void processContentPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasContent()) {
            PointLog logWithContent = point.plusPointByContent(reviewEvent);
            pointLogRep.save(logWithContent);
        }
    }

}

