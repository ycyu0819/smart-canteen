package com.canteen.queue.service;

import com.canteen.common.exception.BusinessException;
import com.canteen.common.response.ResultCode;
import com.canteen.queue.client.OrderClient;
import com.canteen.queue.dto.QueueStatusResponse;
import com.canteen.queue.entity.CallingLog;
import com.canteen.queue.entity.PickupQueue;
import com.canteen.queue.repository.CallingLogRepository;
import com.canteen.queue.repository.PickupQueueRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class QueueService {
    private final StringRedisTemplate redis;
    private final PickupQueueRepository queueRepo;
    private final CallingLogRepository callingLogRepo;
    private final OrderClient orderClient;
    private final ScreenPushService screenPushService;

    public QueueService(StringRedisTemplate redis, PickupQueueRepository queueRepo,
                        CallingLogRepository callingLogRepo, OrderClient orderClient,
                        ScreenPushService screenPushService) {
        this.redis = redis;
        this.queueRepo = queueRepo;
        this.callingLogRepo = callingLogRepo;
        this.orderClient = orderClient;
        this.screenPushService = screenPushService;
    }

    @Transactional
    public void enqueue(Long orderId, Long windowId, String pickupNumber, String pickupCode) {
        String queueKey = "queue:window:" + windowId;
        redis.opsForZSet().add(queueKey, String.valueOf(orderId), System.currentTimeMillis());

        redis.opsForHash().putAll("pickup:code:" + pickupCode,
                Map.of("orderId", String.valueOf(orderId),
                       "windowId", String.valueOf(windowId),
                       "status", "WAITING"));

        PickupQueue pq = new PickupQueue();
        pq.setOrderId(orderId);
        pq.setWindowId(windowId);
        pq.setPickupNumber(pickupNumber);
        pq.setPickupCode(pickupCode);
        pq.setQueueStatus("WAITING");
        pq.setEnqueueTime(LocalDateTime.now());
        queueRepo.save(pq);
    }

    public Map<String, Object> callNext(Long windowId) {
        String queueKey = "queue:window:" + windowId;
        Set<ZSetOperations.TypedTuple<String>> popped = redis.opsForZSet().popMin(queueKey, 1);
        if (popped == null || popped.isEmpty())
            throw new BusinessException(ResultCode.QUEUE_EMPTY);

        Long orderId = Long.valueOf(popped.iterator().next().getValue());
        PickupQueue pq = queueRepo.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(30001, "订单不存在"));

        String callData = String.format(
                "{\"orderId\":%d,\"pickupNumber\":\"%s\",\"callTime\":\"%s\"}",
                orderId, pq.getPickupNumber(), LocalDateTime.now().toString());
        redis.opsForValue().set("current:call:" + windowId, callData);

        pq.setQueueStatus("CALLING");
        pq.setCallCount(pq.getCallCount() + 1);
        pq.setLastCallTime(LocalDateTime.now());
        queueRepo.save(pq);

        CallingLog log = new CallingLog();
        log.setOrderId(orderId);
        log.setWindowId(windowId);
        log.setPickupNumber(pq.getPickupNumber());
        log.setIsRepeat(pq.getCallCount() > 1);
        callingLogRepo.save(log);

        screenPushService.pushCall(windowId, orderId, pq.getPickupNumber());

        return Map.of("orderId", orderId, "pickupNumber", pq.getPickupNumber(),
                "callCount", pq.getCallCount());
    }

    @Transactional
    public void verifyPickup(Long windowId, String pickupCode) {
        Map<Object, Object> data = redis.opsForHash().entries("pickup:code:" + pickupCode);
        if (data.isEmpty()) throw new BusinessException(ResultCode.PICKUP_CODE_INVALID);
        if ("PICKED_UP".equals(data.get("status")))
            throw new BusinessException(ResultCode.PICKUP_CODE_USED);

        Long orderId = Long.valueOf((String) data.get("orderId"));
        redis.opsForZSet().remove("queue:window:" + windowId, String.valueOf(orderId));
        redis.opsForHash().put("pickup:code:" + pickupCode, "status", "PICKED_UP");

        PickupQueue pq = queueRepo.findByOrderId(orderId)
                .orElseThrow(() -> new BusinessException(30001, "订单不存在"));
        pq.setQueueStatus("PICKED_UP");
        pq.setPickedUpTime(LocalDateTime.now());
        queueRepo.save(pq);

        orderClient.pickedUp(orderId);
    }

    public QueueStatusResponse getQueueStatus(Long windowId) {
        String current = redis.opsForValue().get("current:call:" + windowId);
        Set<String> waitingSet = redis.opsForZSet().range("queue:window:" + windowId, 0, -1);
        return new QueueStatusResponse(current, waitingSet,
                waitingSet != null ? waitingSet.size() : 0);
    }

    public Long getMyPosition(Long windowId, Long orderId) {
        return redis.opsForZSet().rank("queue:window:" + windowId, String.valueOf(orderId));
    }
}
