package com.canteen.dish.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "selling_slot")
public class SellingSlot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(name = "slot_type", nullable = false, length = 16)
    private String slotType;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Long getId() { return id; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long v) { dishId = v; }
    public String getSlotType() { return slotType; }
    public void setSlotType(String v) { slotType = v; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime v) { startTime = v; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime v) { endTime = v; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean v) { isActive = v; }
}
