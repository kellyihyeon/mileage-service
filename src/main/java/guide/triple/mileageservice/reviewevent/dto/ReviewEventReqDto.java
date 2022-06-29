package guide.triple.mileageservice.reviewevent.dto;

import guide.triple.mileageservice.reviewevent.entity.ReviewEvent;
import guide.triple.mileageservice.reviewevent.entity.ReviewEventAction;
import lombok.Getter;
import lombok.ToString;
import java.util.List;

@ToString
@Getter
public class ReviewEventReqDto {

    private final String type;
    private final ReviewEventAction action;
    private final String reviewId;
    private final String content;
    private final List<String> attachedPhotoIds;
    private final String userId;
    private final String placeId;

    public ReviewEventReqDto(
                                String type,
                                ReviewEventAction action,
                                String reviewId,
                                String content,
                                List<String> attachedPhotoIds,
                                String userId,
                                String placeId) {
        this.type = type;
        this.action = action;
        this.reviewId = reviewId;
        this.content = content;
        this.attachedPhotoIds = attachedPhotoIds;
        this.userId = userId;
        this.placeId = placeId;
    }

    public ReviewEvent toEntity() {
        return ReviewEvent.builder()
                            .type(type)
                            .action(action)
                            .reviewId(reviewId)
                            .content(content)
                            .attachedPhotoIds(attachedPhotoIds)
                            .userId(userId)
                            .placeId(placeId)
                            .build();
    }
}
