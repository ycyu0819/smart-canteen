package com.canteen.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    @Column(name = "dish_name", nullable = false, length = 128)
    private String dishName;

    @Column(name = "dish_image", length = 256)
    private String dishImage;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer subtotal;

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long v) { orderId = v; }
    public Long getDishId() { return dishId; }
    public void setDishId(Long v) { dishId = v; }
    public String getDishName() { return dishName; }
    public void setDishName(String v) { dishName = v; }
    public String getDishImage() { return dishImage; }
    public void setDishImage(String v) { dishImage = v; }
    public Integer getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Integer v) { unitPrice = v; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer v) { quantity = v; }
    public Integer getSubtotal() { return subtotal; }
    public void setSubtotal(Integer v) { subtotal = v; }
}
