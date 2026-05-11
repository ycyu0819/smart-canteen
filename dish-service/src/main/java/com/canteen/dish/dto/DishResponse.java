package com.canteen.dish.dto;

import com.canteen.dish.entity.Dish;

public class DishResponse {
    private Long id;
    private Long merchantId;
    private Long categoryId;
    private String name;
    private String imageUrl;
    private String description;
    private Integer price;
    private Integer stock;
    private String status;

    public static DishResponse from(Dish d) {
        DishResponse r = new DishResponse();
        r.id = d.getId();
        r.merchantId = d.getMerchantId();
        r.categoryId = d.getCategoryId();
        r.name = d.getName();
        r.imageUrl = d.getImageUrl();
        r.description = d.getDescription();
        r.price = d.getPrice();
        r.stock = d.getStock();
        r.status = d.getStatus();
        return r;
    }

    public Long getId() { return id; }
    public Long getMerchantId() { return merchantId; }
    public Long getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public Integer getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getStatus() { return status; }
}
