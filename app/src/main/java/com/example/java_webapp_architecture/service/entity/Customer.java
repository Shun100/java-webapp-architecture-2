package com.example.java_webapp_architecture.service.entity;

public abstract class Customer {
  /*
   * フィールド
   */
  private int customerId;
  private String customerName;
  private int customerType;
  private String address;
  private int point;

  /*
   * アクセサ
   */
  public void setCustomerId(int customerId) { this.customerId = customerId; }
  public int getCustomerId() { return customerId; }
  public void setCustomerName(String customerName) { this.customerName = customerName; }
  public String getCustomerName() { return customerName; }
  public void setCustomerType(int customerType) { this.customerType = customerType; }
  public int getCustomerType() { return customerType; }

}
