package guide.triple.mileageservice;


import java.util.Arrays;
import java.util.List;


public class ReviewEventReqDtoFixtureBuilder {

    private String type = "REVIEW";
    private ReviewEventAction action = ReviewEventAction.ADD;
    private String reviewId = "240a0658-dc5f-4878-9381-ebb7b2667772";
    private String content = "좋아요 !";
    private List<String> attachedPhotoIds = Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2-851d-4a50-bb07-9cc15cbdc332");
    private String userId = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
    private String placeId = "2e4baf1c-5acb-4efb-a1af-eddada31b00f";

    public static ReviewEventReqDtoFixtureBuilder builder() {
        return new ReviewEventReqDtoFixtureBuilder();
    }

    public ReviewEventReqDtoFixtureBuilder type(String type) {
        this.type = type;
        return this;
    }


    public ReviewEventReqDtoFixtureBuilder action(ReviewEventAction action) {
        this.action = action;
        return this;
    }

    public ReviewEventReqDtoFixtureBuilder reviewId(String reviewId) {
        this.reviewId = reviewId;
        return this;
    }

    public ReviewEventReqDtoFixtureBuilder content(String content) {
        this.content = content;
        return this;
    }

    public ReviewEventReqDtoFixtureBuilder attachedPhotoIds(List<String> attachedPhotoIds) {
        this.attachedPhotoIds = attachedPhotoIds;
        return this;
    }

    public ReviewEventReqDtoFixtureBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public ReviewEventReqDtoFixtureBuilder placeId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    public ReviewEventReqDto build() {
        ReviewEventReqDtoFixtureBuilder builder = builder()
                                                    .type(type)
                                                    .action(action)
                                                    .reviewId(reviewId)
                                                    .content(content)
                                                    .attachedPhotoIds(attachedPhotoIds)
                                                    .userId(userId)
                                                    .placeId(placeId);

        return new ReviewEventReqDto(
                builder.type,
                builder.action,
                builder.reviewId,
                builder.content,
                builder.attachedPhotoIds,
                builder.userId,
                builder.placeId);
    }

    @Override
    public String toString() {
        return "ReviewEventReqDtoFixtureBuilder{" +
                "type='" + type + '\'' +
                ", action=" + action +
                ", reviewId='" + reviewId + '\'' +
                ", content='" + content + '\'' +
                ", attachedPhotoIds=" + attachedPhotoIds +
                ", userId='" + userId + '\'' +
                ", placeId='" + placeId + '\'' +
                '}';
    }
}
