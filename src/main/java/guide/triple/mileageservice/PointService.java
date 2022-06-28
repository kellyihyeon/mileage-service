package guide.triple.mileageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRep;
    private final PointLogRepository pointLogRep;


    // /events
    public void pointEvent(ReviewEventReqDto dto) {
        Point point = pointRep.findByUserId(dto.getUserId());   //"3ede0ef2-92b7-4817-a5f3-0c575361f745"

        ReviewEvent reviewEvent = dto.toEntity();

        // 리뷰 작성: dto 에 리뷰 내용이랑 사진첨부에 따라서 포인트를 준다.
        Point point3 = point.actionCheck(reviewEvent);



        if (ReviewEventAction.ADD.equals(dto.getAction())) {
            if (dto.getContent() != null && dto.getContent().length() >= 1) {
                // 리뷰 조건을 만족하므로 포인트를 만들어준다. createPointLog()
                PointLog logWithContent = PointLog.builder()
                        .point(point)
                        .amount(1)
                        .status(PointStatus.ADDED)
                        .details(PointDetails.REVIEW)
                        .build();

                pointLogRep.save(logWithContent);
            }


            if (!dto.getAttachedPhotoIds().isEmpty()) {
                PointLog logWithPhoto = PointLog.builder()
                        .point(point)
                        .amount(1)
                        .status(PointStatus.ADDED)
                        .details(PointDetails.PHOTO)
                        .build();

                pointLogRep.save(logWithPhoto);
            }

        }

    }
}
