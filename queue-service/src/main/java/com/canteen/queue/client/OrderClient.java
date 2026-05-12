package com.canteen.queue.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PutMapping("/api/order/{id}/picked-up")
    void pickedUp(@PathVariable Long id);
}
