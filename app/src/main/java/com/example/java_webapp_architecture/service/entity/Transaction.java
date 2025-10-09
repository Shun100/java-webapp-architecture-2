package com.example.java_webapp_architecture.service.entity;

import java.util.Date;

public abstract class Transaction {
  /*
   * フィールド
   */
  private int transactionId;    // 取引ID
  private Customer customer;    // 会員
  private Date transactionDate; // 取引日
  private int deliveryCharge;   // 送料

  /*
   * アクセサ
   */
  public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
  public int getTransactionId() { return transactionId; }
  public void setCustomer(Customer customer) { this.customer = customer; }
  public Customer getCustomer() { return customer; }
  public void setTransactionDate(Date transactionDate) { this.transactionDate = transactionDate; }
  public Date getTransactionDate() { return transactionDate; }
  public void setDeliveryCharge(int deliveryCharge) { this.deliveryCharge = deliveryCharge; }
  public int getDeliveryCharge() { return deliveryCharge; }

  /**
   * メソッド
   */

}
