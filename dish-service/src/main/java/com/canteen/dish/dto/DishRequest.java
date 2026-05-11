package com.canteen.dish.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DishRequest {
    @NotBlank
    private String name;
    private String imageUrl;
    private String description;
    @NotNull @Positive
    private Integer price;
    @NotNull
    private Integer stock;
    private Integer alertThreshold;
    private Long categoryId;

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
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long v) { categoryId = v; }
}
