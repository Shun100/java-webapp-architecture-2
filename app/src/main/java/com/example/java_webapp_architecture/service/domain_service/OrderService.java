package com.example.java_webapp_architecture.service.domain_service;

import com.example.java_webapp_architecture.service.entity.Customer;
import com.example.java_webapp_architecture.service.entity.OrderTransaction;

public class OrderService {
  // 注文
  public void placeOrder(OrderTransactionDTO orderTransactionDTO) throws BusinessException {
    // 会員情報の取得
    int customerId = orderTransactionDTO.getCustomerId();
    Customer customer = entityManager.find(Customer.class, customerId);

    // 注文取引の生成
    OrderTransaction orderTransaction = new OrderTransaction();
    orderTransaction.setCustomer(customer);
    orderTransaction.setTransactionDate(orderTransactionDTO.getOrderDate());

    // 注文金額の限度額チェック
    customer.checkTotalPriceLimit(orderTransactionDTO.getTotalPrice());

    // ポイントの加算
    int point = orderTransactionDTO.getTotalPrice() / 10;
    customer.addPoint(point);

    // 送料の計算
    orderTransaction.calcDeliveryCharge();

    // 注文取引の保存
    entityManager.persist(orderTransaction);
  }
}
