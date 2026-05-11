package com.canteen.dish.dto;

public class CategoryRequest {
    private String name;
    private Integer sortOrder;

    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer v) { sortOrder = v; }
}
