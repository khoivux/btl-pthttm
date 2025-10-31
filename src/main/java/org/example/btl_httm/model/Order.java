package org.example.btl_httm.model;

import java.util.Date;

public class Order {
    private int id;
    private float totalPrice;
    private Date createdTime;
    private User user;
    private OrderDetail[] orderDetails;

    public Order(){

    }

    public Order(int id, float totalPrice, Date createdTime, User user, OrderDetail[] orderDetails) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.createdTime = createdTime;
        this.user = user;
        this.orderDetails = orderDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderDetail[] getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetail[] orderDetails) {
        this.orderDetails = orderDetails;
    }
}
