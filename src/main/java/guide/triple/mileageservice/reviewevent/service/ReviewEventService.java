package guide.triple.mileageservice.reviewevent.service;

import guide.triple.mileageservice.*;
import guide.triple.mileageservice.reviewevent.entity.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewEventService {

    private final PointRepository pointRep;
    private final PointLogRepository pointLogRep;


    public void pointEvent(ReviewEvent reviewEvent) {
        Point point = pointRep.findByUserId(reviewEvent.getUserId());
        List<PointLog> pointLogs = pointLogRep.findByPlaceIdAndPointId(reviewEvent.getPlaceId(), point.getId());
        PointLogs pointTransactionLogs = new PointLogs(pointLogs);

        if (reviewEvent.actionIsDelete()) {
            for (PointLog contentLog : pointTransactionLogs.getLogsByContent()) {
                if (pointTransactionLogs.pointTxStatusIsAdded()) {
                    processMinusContentPoint(point, reviewEvent);
                }
            }

            for (PointLog photoLog : pointTransactionLogs.getLogsByPhoto()) {
                if (pointTransactionLogs.pointTxStatusIsAdded()) {
                    processMinusPhotoPoint(point, reviewEvent);
                }
            }

            for (PointLog firstReviewLog : pointTransactionLogs.getLogsByFirstReview()) {
                if (pointTransactionLogs.pointTxStatusIsAdded()) {
                    processMinusFirstReviewPoint(point, reviewEvent);
                }
            }

        }

        if (reviewEvent.isActionMod()) {
            checkContentProcess(pointTransactionLogs.getLogsByContent(), point, reviewEvent);
            checkPhotoProcess(pointTransactionLogs.getLogsByPhoto(), point, reviewEvent);
        }

        if (reviewEvent.isActionAdd()) {
            if (!pointLogRep.existsByPlaceId(reviewEvent.getPlaceId())) {
                processPlusFirstReviewPoint(point, reviewEvent);
            }

            if (!pointTransactionLogs.pointTxStatusIsAdded()) {
                processPlusContentPoint(point, reviewEvent);
                processPlusPhotoPoint(point, reviewEvent);
            }
        }

    }

    private void processMinusFirstReviewPoint(Point point, ReviewEvent reviewEvent) {
        pointLogRep.save(point.minusPointByFirstReview(reviewEvent));
    }

    private void processPlusFirstReviewPoint(Point point, ReviewEvent reviewEvent) {
        pointLogRep.save(point.plusPointByFirstReview(reviewEvent));

    }

    private void checkContentProcess(List<PointLog> contentLogs, Point point, ReviewEvent reviewEvent) {
        for (PointLog contentLog : contentLogs) {
            if (contentLog.statusIsAdded()) {
                processMinusContentPoint(point, reviewEvent);
            } else {
                processPlusContentPoint(point, reviewEvent);
            }
        }
    }

    private void checkPhotoProcess(List<PointLog> photoLogs, Point point, ReviewEvent reviewEvent) {
        for (PointLog photoLog : photoLogs) {
            if (photoLog.statusIsAdded()) {
                processMinusPhotoPoint(point, reviewEvent);
            } else {
                processPlusPhotoPoint(point, reviewEvent);
            }
        }
    }

    private void processMinusPhotoPoint(Point point, ReviewEvent reviewEvent) {
        if (!reviewEvent.hasAttachedPhoto()) {
            pointLogRep.save(point.minusPointByPhoto(reviewEvent));
        }
    }

    private void processMinusContentPoint(Point point, ReviewEvent reviewEvent) {
        if (!reviewEvent.hasContent()) {
            log.info("내용이 {}자 이므로 리뷰 내용 포인트를 회수합니다.", reviewEvent.getContent().length());
            pointLogRep.save(point.minusPointByContent(reviewEvent));
        }
    }

    private void processPlusPhotoPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasAttachedPhoto()) {
            pointLogRep.save(point.plusPointByPhoto(reviewEvent));
        }
    }

    private void processPlusContentPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasContent()) {
            log.info("내용이 {}자 이므로 리뷰 내용 포인트를 적립합니다.", reviewEvent.getContent().length());
            pointLogRep.save(point.plusPointByContent(reviewEvent));
        }
    }

}

