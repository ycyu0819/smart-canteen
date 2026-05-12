package com.canteen.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequest {
    @NotNull
    private Long dishId;
    @NotNull @Positive
    private Integer quantity;

    public Long getDishId() { return dishId; }
    public void setDishId(Long v) { dishId = v; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer v) { quantity = v; }
}
