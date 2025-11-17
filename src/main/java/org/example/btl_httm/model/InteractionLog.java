package org.example.btl_httm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InteractionLog {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("action")
    private String actionType;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("category_code")
    private String categoryCode;
    public InteractionLog() {
    }

    public InteractionLog(String userId, String productId, String actionType, String brand, String categoryCode) {
        this.userId = userId;
        this.productId = productId;
        this.actionType = actionType;
        this.brand = brand;
        this.categoryCode = categoryCode;
    }

    public InteractionLog(String productId, String actionType, String brand, String categoryCode) {
        this.productId = productId;
        this.actionType = actionType;
        this.brand = brand;
        this.categoryCode = categoryCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String toString() {
        return "InteractionLog{" +
                "userId='" + userId + '\'' +
                ", productId='" + productId + '\'' +
                ", actionType='" + actionType + '\'' +
                ", brand='" + brand + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                '}';
    }
}
