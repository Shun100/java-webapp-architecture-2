package com.example.java_webapp_architecture.service.domain_service;

import com.example.java_webapp_architecture.service.entity.Customer;
import com.example.java_webapp_architecture.service.entity.Transaction;
import com.example.java_webapp_architecture.service.entity.ReturnTransaction;

public class ReturnService {
  // 返品する
  public void returnProduct(ReturnTransactionDTO returnTransactionDTO) throws BuisinessException {
    // 会員情報の取得
    int customerId = returnTransactionDTO.getCustomerId();
    Customer customer = entityManager.find(Customer.class, customerId);

    // 商品情報の取得
    Product product = null;

    // 注文取引の取得
    Transaction returnTransaction = new ReturnTransaction();

    // 返送料の計算
    returnTransaction.calcDeliveryCharge();

    // 返品取引の保存
    entityManager.persist(returnTransaction);
  }
}
