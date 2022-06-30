package guide.triple.mileageservice.reviewevent.controller;

import guide.triple.mileageservice.ApiResponse;
import guide.triple.mileageservice.reviewevent.service.HistoryDto;
import guide.triple.mileageservice.reviewevent.service.MyPointHistory;
import guide.triple.mileageservice.reviewevent.service.ReviewEventService;
import guide.triple.mileageservice.reviewevent.dto.ReviewEventReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class ReviewEventController {

    private final ReviewEventService service;


    @PostMapping
    public String pointEvent(@RequestBody ReviewEventReqDto dto) {
        log.info("리뷰 포인트 이벤트 발생 - {}", dto);
        service.pointEvent(dto.toEntity());
        return "200, OK";
    }

    @GetMapping()
    public ApiResponse<MyPointHistory<HistoryDto>> getMyPoint() {
        return ApiResponse.ok(service.getMyPoint());
    }



}
