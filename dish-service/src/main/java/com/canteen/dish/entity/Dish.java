package com.canteen.dish.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Dish {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Column(length = 512)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "alert_threshold", nullable = false)
    private Integer alertThreshold = 10;

    @Column(nullable = false, length = 16)
    private String status = "OFF_SHELF";

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long v) { merchantId = v; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long v) { categoryId = v; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String v) { imageUrl = v; }
    public String getDescription() { return description; }
    public void setDescription(String v) { description = v; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer v) { price = v; }
    public Integer getStock() { return stock; }
    public void setStock(Integer v) { stock = v; }
    public Integer getAlertThreshold() { return alertThreshold; }
    public void setAlertThreshold(Integer v) { alertThreshold = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { status = v; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean v) { isDeleted = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
