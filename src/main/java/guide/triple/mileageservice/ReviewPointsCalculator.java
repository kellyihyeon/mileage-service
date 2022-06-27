package guide.triple.mileageservice;

import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ReviewPointsCalculator {

    private List<Points> points;

    private int totalPoints;

    public ReviewPointsCalculator() {
        totalPoints = 0;
    }

    public void calculate(ReviewEventReqDto eventDto) {
        if (eventDto.getAction().equals(ReviewEventAction.ADD)) {
            reviewChecker(eventDto);
        }
    }

    private void reviewChecker(ReviewEventReqDto eventDto) {
        if (eventDto.getContent() != null && eventDto.getContent().length() >= 1) {
            totalPoints += 1;
            log.info("[review rewards] 리뷰 내용 작성: 1점[글자수 {}자]", eventDto.getContent().length());
        }

        if (!eventDto.getAttachedPhotoIds().isEmpty()) {
            totalPoints += 1;
            log.info("[review rewards] 사진 첨부: 1점[{}]", !eventDto.getAttachedPhotoIds().isEmpty());
        }
    }


    public List<Points> check(ReviewEventReqDto dto) {
        List<Points> points = new ArrayList<>();

        if (hasContent(dto)) {
            points.add(new Points(1, PointStatus.PLUS, PointDetails.REVIEW));
        }

        if (hasPhoto(dto)) {
            points.add(new Points(1, PointStatus.PLUS, PointDetails.PHOTO));
        }

        return points;
    }

    private boolean hasPhoto(ReviewEventReqDto dto) {
        return !dto.getAttachedPhotoIds().isEmpty();
    }

    private boolean hasContent(ReviewEventReqDto dto) {
        return dto.getContent() != null && dto.getContent().trim().length() >= 1;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
