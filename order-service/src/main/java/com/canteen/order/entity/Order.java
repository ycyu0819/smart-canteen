package com.canteen.order.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_tbl")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false, length = 32)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(nullable = false, length = 16)
    private String status;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "pickup_number", length = 16)
    private String pickupNumber;

    @Column(name = "pickup_code", length = 8)
    private String pickupCode;

    @Column(name = "cancel_reason", length = 128)
    private String cancelReason;

    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "prepared_at")
    private LocalDateTime preparedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String v) { orderNo = v; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { userId = v; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long v) { merchantId = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public Integer getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Integer v) { totalPrice = v; }
    public String getPickupNumber() { return pickupNumber; }
    public void setPickupNumber(String v) { pickupNumber = v; }
    public String getPickupCode() { return pickupCode; }
    public void setPickupCode(String v) { pickupCode = v; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String v) { cancelReason = v; }
    public LocalDateTime getCancelTime() { return cancelTime; }
    public void setCancelTime(LocalDateTime v) { cancelTime = v; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime v) { paidAt = v; }
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime v) { acceptedAt = v; }
    public LocalDateTime getPreparedAt() { return preparedAt; }
    public void setPreparedAt(LocalDateTime v) { preparedAt = v; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime v) { completedAt = v; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime v) { cancelledAt = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
