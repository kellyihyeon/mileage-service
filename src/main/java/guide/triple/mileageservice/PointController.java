package guide.triple.mileageservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService service;


    @PostMapping("/events")
    public String pointEvent(@RequestBody ReviewEventReqDto dto) {
        log.info("리뷰 포인트 이벤트 발생 - {}", dto);
        service.pointEvent(dto.toEntity());

        return "200, OK";
    }


}
