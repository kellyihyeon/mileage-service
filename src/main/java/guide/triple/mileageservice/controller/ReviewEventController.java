package guide.triple.mileageservice.controller;

import guide.triple.mileageservice.common.ApiResponse;
import guide.triple.mileageservice.dto.HistoryDto;
import guide.triple.mileageservice.dto.MyPointHistory;
import guide.triple.mileageservice.service.PointService;
import guide.triple.mileageservice.service.ReviewEventService;
import guide.triple.mileageservice.dto.ReviewEventReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class ReviewEventController {

    private final ReviewEventService reviewEventService;
    private final PointService pointService;


    @PostMapping
    public String createEvent(@RequestBody ReviewEventReqDto dto) {
        log.info("리뷰 포인트 이벤트 발생 - {}", dto);
        reviewEventService.createEvent(dto.toEntity());
        return "200, OK";
    }

    @GetMapping()
    public ApiResponse<MyPointHistory<HistoryDto>> getMyPoint() {
        return ApiResponse.ok(pointService.getMyPoint());
    }



}
