package com.canteen.queue.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickup_queue")
public class PickupQueue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "window_id", nullable = false)
    private Long windowId;

    @Column(name = "pickup_number", length = 16)
    private String pickupNumber;

    @Column(name = "pickup_code", length = 8)
    private String pickupCode;

    @Column(name = "queue_status", nullable = false, length = 16)
    private String queueStatus;

    @Column(name = "enqueue_time", nullable = false)
    private LocalDateTime enqueueTime;

    @Column(name = "call_count")
    private Integer callCount = 0;

    @Column(name = "last_call_time")
    private LocalDateTime lastCallTime;

    @Column(name = "picked_up_time")
    private LocalDateTime pickedUpTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long v) { orderId = v; }
    public Long getWindowId() { return windowId; }
    public void setWindowId(Long v) { windowId = v; }
    public String getPickupNumber() { return pickupNumber; }
    public void setPickupNumber(String v) { pickupNumber = v; }
    public String getPickupCode() { return pickupCode; }
    public void setPickupCode(String v) { pickupCode = v; }
    public String getQueueStatus() { return queueStatus; }
    public void setQueueStatus(String v) { queueStatus = v; }
    public LocalDateTime getEnqueueTime() { return enqueueTime; }
    public void setEnqueueTime(LocalDateTime v) { enqueueTime = v; }
    public Integer getCallCount() { return callCount; }
    public void setCallCount(Integer v) { callCount = v; }
    public LocalDateTime getLastCallTime() { return lastCallTime; }
    public void setLastCallTime(LocalDateTime v) { lastCallTime = v; }
    public LocalDateTime getPickedUpTime() { return pickedUpTime; }
    public void setPickedUpTime(LocalDateTime v) { pickedUpTime = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
