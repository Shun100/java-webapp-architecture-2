package com.example.java_webapp_architecture.dto;

import com.example.java_webapp_architecture.constant.Const;

public record PersonDto(
  int personId,
  String personName,
  int age,
  String gender
) {

  public PersonDto() {
    this(Const.ID_NOT_REGISTERED, "", Const.AGE_NOT_REGISTERED, null);
  }
}
