package com.canteen.queue.controller;

import com.canteen.common.response.Result;
import com.canteen.queue.dto.QueueStatusResponse;
import com.canteen.queue.service.QueueService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/queue")
public class QueueController {
    private final QueueService queueService;

    public QueueController(QueueService queueService) { this.queueService = queueService; }

    @PostMapping("/enqueue")
    public void enqueue(@RequestBody Map<String, Object> req) {
        queueService.enqueue(
                Long.valueOf(req.get("orderId").toString()),
                Long.valueOf(req.get("windowId").toString()),
                (String) req.get("pickupNumber"),
                (String) req.get("pickupCode"));
    }

    @PostMapping("/call")
    public Result<Map<String, Object>> callNext(@RequestBody Map<String, Object> req) {
        Long windowId = Long.valueOf(req.get("windowId").toString());
        return Result.success(queueService.callNext(windowId));
    }

    @PostMapping("/verify")
    public Result<Void> verify(@RequestBody Map<String, String> req) {
        queueService.verifyPickup(
                Long.valueOf(req.get("windowId")),
                req.get("pickupCode"));
        return Result.success();
    }

    @GetMapping("/window/{windowId}")
    public Result<QueueStatusResponse> windowStatus(@PathVariable Long windowId) {
        return Result.success(queueService.getQueueStatus(windowId));
    }

    @GetMapping("/my-position/{orderId}")
    public Result<Long> myPosition(@PathVariable Long orderId,
                                   @RequestParam Long windowId) {
        return Result.success(queueService.getMyPosition(windowId, orderId));
    }
}
