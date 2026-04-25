package com.devsuperior.dscommerce.projections;

import com.devsuperior.dscommerce.entities.OrderStatus;

import java.time.Instant;

public class OrderProjectionImpl implements OrderProjection {

    private Long orderId;
    private Instant orderMomenent;
    private OrderStatus orderStatus;
    private Long clientId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private String clientName;
    private Long productId;
    private String productImgUrl;
    private Long paymentId;
    private Instant paymentMoment;

    public OrderProjectionImpl() {
    }

    public OrderProjectionImpl(
        Long orderId, Instant orderMoment, OrderStatus orderStatus, Long clientId, String clientName,
        Long productId, String productName, Double productPrice, String productImgUrl, Integer quantity,
        Long paymentId, Instant paymentMoment
    ) {
        this.orderId = orderId;
        this.orderMomenent = orderMoment;
        this.orderStatus = orderStatus;
        this.clientId = clientId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.clientName = clientName;
        this.productId = productId;
        this.productImgUrl = productImgUrl;
        this.paymentId = paymentId;
        this.paymentMoment = paymentMoment;
    }

    @Override
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public Instant getOrderMoment() {
        return orderMomenent;
    }

    public void setOrderMomenent(Instant orderMomenent) {
        this.orderMomenent = orderMomenent;
    }

    @Override
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String getProductImgUrl() {
        return productImgUrl;
    }

    public void setProductImgUrl(String productImgUrl) {
        this.productImgUrl = productImgUrl;
    }

    @Override
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public Instant getPaymentMoment() {
        return paymentMoment;
    }

    public void setPaymentMoment(Instant paymentMoment) {
        this.paymentMoment = paymentMoment;
    }

}
