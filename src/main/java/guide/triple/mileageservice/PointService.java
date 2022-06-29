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
     * 포인트 취소시키는 로직 짜자.
     */
    public void pointEvent(ReviewEvent reviewEvent) {
        Point point = pointRep.findByUserId(reviewEvent.getUserId());

        if (reviewEvent.isActionMod()) {
            List<PointLog> pointLogs = pointLogRep.findByPlaceId(reviewEvent.getPlaceId()); // 검색 인덱스 지정
            PointLogs pointTransactionLogs = new PointLogs(pointLogs);

            List<PointLog> contentLogs = pointTransactionLogs.getLogsByContent();
            for (PointLog contentLog : contentLogs) {
                log.info("[MOD] content log is existed. status is {}.", contentLog.getStatus());
                if (contentLog.statusIsAdded()) {
                    processMinusContentPoint(point, reviewEvent);
                } else {  // CANCELED
                    processPlusContentPoint(point, reviewEvent);
                }
            }

        }

        if (reviewEvent.isActionAdd()) {
            processPlusContentPoint(point, reviewEvent);
            processPhotoPoint(point, reviewEvent);
        }

    }

    private void processMinusContentPoint(Point point, ReviewEvent reviewEvent) {
        if (!reviewEvent.hasContent()) {
            log.info("내용이 {}자 이므로 리뷰 내용 포인트를 회수합니다.", reviewEvent.getContent().length());
            pointLogRep.save(point.minusPointByContent(reviewEvent));
        }
    }

    private void processPhotoPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasAttachedPhoto()) {
            PointLog logWithPhoto = point.plusPointByPhoto(reviewEvent);
            pointLogRep.save(logWithPhoto);
        }
    }

    private void processPlusContentPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasContent()) {
            log.info("내용이 {}자 이므로 리뷰 내용 포인트를 적립합니다.", reviewEvent.getContent().length());
            pointLogRep.save(point.plusPointByContent(reviewEvent));
        }
    }

}

