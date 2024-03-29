package guide.triple.mileageservice.service;

import guide.triple.mileageservice.entity.Point;
import guide.triple.mileageservice.entity.PointDetails;
import guide.triple.mileageservice.entity.PointLog;
import guide.triple.mileageservice.entity.PointLogs;
import guide.triple.mileageservice.repository.PointLogRepository;
import guide.triple.mileageservice.repository.PointRepository;
import guide.triple.mileageservice.entity.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewEventService {

    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;

    @Transactional
    public Boolean createEvent(ReviewEvent reviewEvent) {
        Point point = pointRepository.findByUserId(reviewEvent.getUserId());
        List<PointLog> pointLogs = pointLogRepository.findByPlaceIdAndPointId(reviewEvent.getPlaceId(), point.getId());
        PointLogs pointTransactionLogs = new PointLogs(pointLogs);

        if (reviewEvent.isActionAdd()) {
            processActionAdd(reviewEvent, point, pointTransactionLogs);
        }

        if (reviewEvent.isActionMod()) {
            processActionMod(reviewEvent, point, pointTransactionLogs);
        }

        if (reviewEvent.isActionDelete()) {
            processActionDelete(reviewEvent, point, pointTransactionLogs);
        }

        return true;
    }

    private void processActionDelete(ReviewEvent reviewEvent, Point point, PointLogs pointTransactionLogs) {
        checkFirstReviewByPlaceAndUser(pointTransactionLogs, point, reviewEvent);
        checkAddedPointTxByContent(pointTransactionLogs, point, reviewEvent);
        checkAddedPointTxByPhoto(pointTransactionLogs, point, reviewEvent);
    }

    private void processActionMod(ReviewEvent reviewEvent, Point point, PointLogs pointTransactionLogs) {
        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT)) {
            processMinusContentPoint(point, reviewEvent);
        } else {
            processPlusContentPoint(point, reviewEvent);
        }

        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.PHOTO)) {
            processMinusPhotoPoint(point, reviewEvent);
        } else {
            processPlusPhotoPoint(point, reviewEvent);
        }
    }

    private void processActionAdd(ReviewEvent reviewEvent, Point point, PointLogs pointTransactionLogs) {
        checkDuplicatedReviewAction(pointTransactionLogs);
        checkFirstReviewByPlace(point, reviewEvent);

        processPlusContentPoint(point, reviewEvent);
        processPlusPhotoPoint(point, reviewEvent);
    }

    private void checkFirstReviewByPlaceAndUser(PointLogs pointTransactionLogs, Point point, ReviewEvent reviewEvent) {
        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.FIRST_REVIEW)) {
            processMinusFirstReviewPoint(point, reviewEvent);
        }
    }

    private void checkAddedPointTxByPhoto(PointLogs pointTransactionLogs, Point point, ReviewEvent reviewEvent) {
        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.PHOTO)) {
            processMinusPhotoPoint(point, reviewEvent);
        }
    }

    private void checkAddedPointTxByContent(PointLogs pointTransactionLogs, Point point, ReviewEvent reviewEvent) {
        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT)) {
            processMinusContentPoint(point, reviewEvent);
        }
    }

    private void processByContent(PointLogs pointTransactionLogs, Point point, ReviewEvent reviewEvent) {
        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT)) {
            processMinusContentPoint(point, reviewEvent);
        }
    }

    private void checkDuplicatedReviewAction(PointLogs pointTransactionLogs) {
        if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT) ||
                pointTransactionLogs.pointTxStatusIsAdded(PointDetails.PHOTO)) {
            throw new IllegalArgumentException("한 사용자는 장소마다 리뷰를 하나만 작성할 수 있습니다.");
        }
    }

    private void checkFirstReviewByPlace(Point point, ReviewEvent reviewEvent) {
        PointLogs pointLogsByPlace = new PointLogs(pointLogRepository.findByPlaceId(reviewEvent.getPlaceId()));

        if (!pointLogsByPlace.pointTxStatusIsAdded(PointDetails.FIRST_REVIEW)) {
            processPlusFirstReviewPoint(point, reviewEvent);
        }
    }

    private void processMinusFirstReviewPoint(Point point, ReviewEvent reviewEvent) {
        pointLogRepository.save(point.minusPointByFirstReview(reviewEvent));
    }

    private void processPlusFirstReviewPoint(Point point, ReviewEvent reviewEvent) {
        pointLogRepository.save(point.plusPointByFirstReview(reviewEvent));

    }

    private void processMinusPhotoPoint(Point point, ReviewEvent reviewEvent) {
        if (!reviewEvent.hasAttachedPhoto()) {
            pointLogRepository.save(point.minusPointByPhoto(reviewEvent));
        }
    }

    private void processMinusContentPoint(Point point, ReviewEvent reviewEvent) {
        if (!reviewEvent.hasContent()) {
            log.info("내용이 {}자 이므로 리뷰 내용 포인트를 회수합니다.", reviewEvent.getContent().length());
            pointLogRepository.save(point.minusPointByContent(reviewEvent));
        }
    }

    private void processPlusPhotoPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasAttachedPhoto()) {
            pointLogRepository.save(point.plusPointByPhoto(reviewEvent));
        }
    }

    private void processPlusContentPoint(Point point, ReviewEvent reviewEvent) {
        if (reviewEvent.hasContent()) {
            log.info("내용이 {}자 이므로 리뷰 내용 포인트를 적립합니다.", reviewEvent.getContent().length());
            pointLogRepository.save(point.plusPointByContent(reviewEvent));
        }
    }
}
