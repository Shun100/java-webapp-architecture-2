package com.example.java_webapp_architecture.service.entity;

import java.util.List;

public class OrderTransaction extends Transaction {
  /*
   * フィールド
   */
  private List<OrderDetail> orderDetails;
  private int totalPrice;

  /**
   * アクセサ
   */
  public void setOrderDeails(List<OrederDetail> orderDetails) { this.orderDetails = orderDetails; }
  public List<OrderDetail> getOrderDetails() { return orderDetails; }
  public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
  public int getTotalPrice() {return totalPrice; }

  /**
   * メソッド
   */
  @Override
  public void calcDeliveryCharge() {
    if (totalPrice >= 5000) {
      setDeliveryCharge(0);
    } else {
      setDeliveryCharge(getDeliveryChargeByAddress());
    }
  }
}
