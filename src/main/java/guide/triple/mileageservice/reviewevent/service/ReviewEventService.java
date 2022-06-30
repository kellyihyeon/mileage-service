package guide.triple.mileageservice.reviewevent.service;

import guide.triple.mileageservice.*;
import guide.triple.mileageservice.reviewevent.entity.ReviewEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewEventService {

    private final PointRepository pointRep;
    private final PointLogRepository pointLogRep;


    @Transactional
    public void pointEvent(ReviewEvent reviewEvent) {
        Point point = pointRep.findByUserId(reviewEvent.getUserId());
        List<PointLog> pointLogs = pointLogRep.findByPlaceIdAndPointId(reviewEvent.getPlaceId(), point.getId());
        PointLogs pointTransactionLogs = new PointLogs(pointLogs);

        if (reviewEvent.actionIsDelete()) {
            if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.FIRST_REVIEW)) {
                processMinusFirstReviewPoint(point, reviewEvent);
            }

            if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT)) {
                processMinusContentPoint(point, reviewEvent);
            }

            if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.PHOTO)) {
                processMinusPhotoPoint(point, reviewEvent);
            }

        }

        if (reviewEvent.isActionMod()) {
            if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT)) {
                processMinusContentPoint(point, reviewEvent);// 컨텐트 트랜잭션이 added 일 때 컨텐트 마이너스
            }else {
                processPlusContentPoint(point, reviewEvent);// 컨텐트 트랜잭션이 canceled 일 때 컨텐트 플러스
            }

            if (pointTransactionLogs.pointTxStatusIsAdded(PointDetails.PHOTO)) {
                processMinusPhotoPoint(point, reviewEvent);
            }else {
                processPlusPhotoPoint(point, reviewEvent);
            }
        }

        if (reviewEvent.isActionAdd()) {
            if (!pointTransactionLogs.pointTxStatusIsAdded(PointDetails.FIRST_REVIEW)) {
                processPlusFirstReviewPoint(point, reviewEvent);
            }

            if (!pointTransactionLogs.pointTxStatusIsAdded(PointDetails.CONTENT)) {
                processPlusContentPoint(point, reviewEvent);
            }   // 컨텐트 트랜잭션이 canceled 일 때 컨텐트 플러스

            if (!pointTransactionLogs.pointTxStatusIsAdded(PointDetails.PHOTO)) {
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


    public MyPointHistory<HistoryDto> getMyPoint() {
        String concurrentUserId = "8crf2ew6-25r3-3492-y2w0-2f582458z024";

        Point point = pointRep.findByUserId(concurrentUserId);
        List<PointLog> logs = pointLogRep.findByPoint(point);
        PointLogs pointLogs = new PointLogs(logs);

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
        return new MyPointHistory<>(historyDtos, point.getTotalPoint());
    }
}

