package guide.triple.mileageservice;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReviewPointEvent {

    private final List<Point> pointHistory = new ArrayList<>();

    private int totalPoint = 0;


    /**
     * dto 를 검사해서 적립해줄 건지 취소할 건지
     * @param dto contains fields like type, action, reviewId, content, attachedPhotoIds, userId, placeId,
     * @return    maybe modified.
     */
    public List<Point> check(ReviewEventReqDto dto) {
        if (hasContent(dto)) {
            pointHistory.add(new Point(1, PointStatus.ADDED, PointDetails.REVIEW));    //추가: reviewId
            totalPoint += 1;
            log.info("[review rewards] 리뷰 내용 작성: 1점[글자수 {}자]", dto.getContent().length());
        }

        if (hasPhoto(dto)) {
            pointHistory.add(new Point(1, PointStatus.ADDED, PointDetails.PHOTO));
            totalPoint += 1;
            log.info("[review rewards] 사진 첨부: 1점[{}]", !dto.getAttachedPhotoIds().isEmpty());
        }

        return pointHistory;
    }

    private boolean hasPhoto(ReviewEventReqDto dto) {
        return !dto.getAttachedPhotoIds().isEmpty();
    }

    private boolean hasContent(ReviewEventReqDto dto) {
        return dto.getContent() != null && dto.getContent().trim().length() >= 1;
    }

    public int getTotalPoint() {
        return totalPoint;
    }
}
