package com.canteen.order.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_log")
public class OrderStatusLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "from_status", length = 16)
    private String fromStatus;

    @Column(name = "to_status", nullable = false, length = 16)
    private String toStatus;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "operator_type", length = 16)
    private String operatorType;

    @Column(length = 256)
    private String remark;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long v) { orderId = v; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String v) { fromStatus = v; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String v) { toStatus = v; }
    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long v) { operatorId = v; }
    public String getOperatorType() { return operatorType; }
    public void setOperatorType(String v) { operatorType = v; }
    public String getRemark() { return remark; }
    public void setRemark(String v) { remark = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
