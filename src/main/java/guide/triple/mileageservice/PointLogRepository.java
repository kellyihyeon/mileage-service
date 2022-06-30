package guide.triple.mileageservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    List<PointLog> findByPlaceIdAndPointId(String placeId, Long pointId);

    Boolean existsByPlaceId(String placeId);

    List<PointLog> findByPointId(Long pointId);

    List<PointLog> findByPoint(Point point);
}
