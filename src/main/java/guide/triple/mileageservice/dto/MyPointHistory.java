package guide.triple.mileageservice.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class MyPointHistory<T> {

    private final int totalPoint;
    private final List<T> histories;

    public MyPointHistory(List<T> logs, int totalPoint) {
        this.histories = logs;
        this.totalPoint = totalPoint;
    }



}
