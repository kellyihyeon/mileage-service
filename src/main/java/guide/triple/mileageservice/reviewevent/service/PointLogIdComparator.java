package guide.triple.mileageservice.reviewevent.service;

import java.util.Comparator;

public class PointLogIdComparator implements Comparator<HistoryDto> {

    @Override
    public int compare(HistoryDto o1, HistoryDto o2) {
        return o1.getPointLogId().compareTo(o2.getPointLogId());
    }
}
