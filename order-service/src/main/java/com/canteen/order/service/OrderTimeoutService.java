package com.canteen.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class OrderTimeoutService implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutService.class);
    private static final String PREFIX = "order:timeout:";

    private final StringRedisTemplate redisTemplate;
    private final OrderService orderService;

    public OrderTimeoutService(StringRedisTemplate redisTemplate, OrderService orderService) {
        this.redisTemplate = redisTemplate;
        this.orderService = orderService;
    }

    public void scheduleTimeout(Long orderId, int delayMinutes) {
        redisTemplate.opsForValue().set(PREFIX + orderId,
                String.valueOf(orderId), delayMinutes, TimeUnit.MINUTES);
    }

    public void cancelTimeout(Long orderId) {
        redisTemplate.delete(PREFIX + orderId);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if (key.startsWith(PREFIX)) {
            Long orderId = Long.valueOf(key.substring(PREFIX.length()));
            log.info("Order timeout: {}", orderId);
            try {
                orderService.cancelByTimeout(orderId);
            } catch (Exception e) {
                log.error("Failed to auto-cancel order {}", orderId, e);
            }
        }
    }
}
