package com.canteen.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "queue-service")
public interface QueueClient {

    @PostMapping("/api/queue/enqueue")
    void enqueue(@RequestBody Map<String, Object> queueRequest);
}
