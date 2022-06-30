package guide.triple.mileageservice.reviewevent.service;

import lombok.Getter;
import java.util.List;

@Getter
public class MyPointHistory<T> {

    private final List<T> histories;
    private final int totalPoint;

    public MyPointHistory(List<T> logs, int totalPoint) {
        this.histories = logs;
        this.totalPoint = totalPoint;
    }



}
