package guide.triple.mileageservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

    Point findByUserId(String userId);
}
