package com.canteen.dish.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_log")
public class StockLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(name = "change_type", nullable = false, length = 16)
    private String changeType;

    @Column(nullable = false)
    private Integer delta;

    @Column(name = "before_stock", nullable = false)
    private Integer beforeStock;

    @Column(name = "after_stock", nullable = false)
    private Integer afterStock;

    @Column(name = "related_order_id")
    private Long relatedOrderId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long v) { dishId = v; }
    public String getChangeType() { return changeType; }
    public void setChangeType(String v) { changeType = v; }
    public Integer getDelta() { return delta; }
    public void setDelta(Integer v) { delta = v; }
    public Integer getBeforeStock() { return beforeStock; }
    public void setBeforeStock(Integer v) { beforeStock = v; }
    public Integer getAfterStock() { return afterStock; }
    public void setAfterStock(Integer v) { afterStock = v; }
    public Long getRelatedOrderId() { return relatedOrderId; }
    public void setRelatedOrderId(Long v) { relatedOrderId = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
