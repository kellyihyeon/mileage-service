package guide.triple.mileageservice.common;

import guide.triple.mileageservice.dto.HistoryDto;
import java.util.Comparator;

public class PointLogIdComparator implements Comparator<HistoryDto> {

    @Override
    public int compare(HistoryDto o1, HistoryDto o2) {
        return o1.getPointLogId().compareTo(o2.getPointLogId());
    }
}
