package com.example.java_webapp_architecture.service.entity;

public class ReturnTransaction extends Transaction {
  /*
   * フィールド
   */
  private Product product; // 返品対象の商品
  private OrderTransaction originalOrderTransaction; // 注文時の取引情報

  /*
   * アクセサ
   */
  public void setProduct(Product product) { this.product = product; }
  public Product getProdcut() { return product; }
  public void setOriginalOrderTransaction(OrderTransaction originalOrderTransaction) {
    this.originalOrderTransaction = originalOrderTransaction;
  }
  public OrderTransaction getOriginalOrderTransaction() {
    return originalOrderTransaction;
  }

  /*
   * メソッド
   */
  @Override
  public void calcDeliveryCharge() {
    setDeliveryCharge(getDeliveryChargeByAddress());
  }
}
