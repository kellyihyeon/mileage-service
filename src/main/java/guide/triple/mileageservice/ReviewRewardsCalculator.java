package guide.triple.mileageservice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReviewRewardsCalculator {
    private static int rewards = 0;


    public void calculate(ReviewEventReqDto eventDto) {
        if (eventDto.getContent() != null && eventDto.getContent().length() >= 1) {
            rewards += 1;
            log.info("[review rewards] 리뷰 내용 작성: 1점[글자수 {}자]", eventDto.getContent().length());
        }

        if (!eventDto.getAttachedPhotoIds().isEmpty()) {
            rewards += 1;
            log.info("[review rewards] 사진 첨부: 1점[{}]", !eventDto.getAttachedPhotoIds().isEmpty());
        }

    }

    public int getRewards() {
        return rewards;
    }
}
