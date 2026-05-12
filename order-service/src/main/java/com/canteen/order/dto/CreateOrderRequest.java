package com.canteen.order.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CreateOrderRequest {
    @NotNull
    private Long merchantId;
    @NotNull
    private List<OrderItemRequest> items;

    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long v) { merchantId = v; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> v) { items = v; }
}
