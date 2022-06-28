package guide.triple.mileageservice;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import java.util.List;

@ToString
@Getter
@Builder
@RequiredArgsConstructor
public class ReviewEvent {

    private final String type;
    private final ReviewEventAction action;
    private final String reviewId;
    private final String content;
    private final List<String> attachedPhotoIds;
    private final String userId;
    private final String placeId;


    public boolean isActionAdd() {
        return ReviewEventAction.ADD.equals(action);
    }

    public boolean hasContent() {
        if (getContent() == null) {
            throw new IllegalArgumentException("리뷰 내용은 1글자 이상이어야 합니다.");
        }

        return getContent() != null && getContent().length() >= 1;
    }

    public boolean hasAttachedPhoto() {
        return !attachedPhotoIds.isEmpty();
    }

    public boolean isActionMod() {
        return ReviewEventAction.MOD.equals(action);
    }

    public boolean isActionDelete() {
        return ReviewEventAction.DELETE.equals(action);
    }
}
