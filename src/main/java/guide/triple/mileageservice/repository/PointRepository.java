package guide.triple.mileageservice.repository;

import guide.triple.mileageservice.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

    Point findByUserId(String userId);
}
