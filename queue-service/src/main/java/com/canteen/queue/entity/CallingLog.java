package com.canteen.queue.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calling_log")
public class CallingLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "window_id", nullable = false)
    private Long windowId;

    @Column(name = "pickup_number", length = 16)
    private String pickupNumber;

    @Column(name = "call_time", nullable = false)
    private LocalDateTime callTime;

    @Column(name = "is_repeat", nullable = false)
    private Boolean isRepeat = false;

    @PrePersist void onCreate() { callTime = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long v) { orderId = v; }
    public Long getWindowId() { return windowId; }
    public void setWindowId(Long v) { windowId = v; }
    public String getPickupNumber() { return pickupNumber; }
    public void setPickupNumber(String v) { pickupNumber = v; }
    public LocalDateTime getCallTime() { return callTime; }
    public void setCallTime(LocalDateTime v) { callTime = v; }
    public Boolean getIsRepeat() { return isRepeat; }
    public void setIsRepeat(Boolean v) { isRepeat = v; }
}
