package org.example.btl_httm.model;

public class InteractionLog {
    public String user_id;
    public String product_id;
    public String action_type;
    public String brand;
    public String category_code;
    public InteractionLog() {
    }
    public InteractionLog(String user_id, String product_id, String action_type, String brand, String category_code) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.action_type = action_type;
        this.brand = brand;
        this.category_code = category_code;
    }

    public InteractionLog(String product_id, String action_type, String brand, String category_code) {
        this.product_id = product_id;
        this.action_type = action_type;
        this.brand = brand;
        this.category_code = category_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getAction() {
        return action_type;
    }

    public void setAction(String action) {
        this.action_type = action;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String toString() {
        return "InteractionLog{" +
                "product_id='" + product_id + '\'' +
                ", action='" + action_type + '\'' +
                ", brand='" + brand + '\'' +
                ", category_code='" + category_code + '\'' +
                '}';
    }
}
