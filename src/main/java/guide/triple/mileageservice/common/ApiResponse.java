package guide.triple.mileageservice.common;


import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {

    private final int statusCode;
    private final T result;
    private final LocalDateTime timeStamp;

    public ApiResponse(int statusCode, T result, LocalDateTime timeStamp) {
        this.statusCode = statusCode;
        this.result = result;
        this.timeStamp = timeStamp;
    }


    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>(200, result, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> conflict(T result) {
        return new ApiResponse<>(409, result, LocalDateTime.now());
    }
}
