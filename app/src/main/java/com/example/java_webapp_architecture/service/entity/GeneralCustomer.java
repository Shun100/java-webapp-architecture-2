package com.example.java_webapp_architecture.service.entity;

public class GeneralCustomer extends Customer {
  // 購入金額の限度額チェック
  @Override
  public void checkTotalPriceLimit(int totalPrice) throws BusinessException {
    if (10000 < totalPrice) {
      throw new BusinessException("限度額オーバー");
    }
  }

  // ポイントを計算して加算する
  @Override
  public void addPoint(int point) {
    setPoint(point);
  }
}
