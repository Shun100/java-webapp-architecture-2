package com.example.java_webapp_architecture.dto;

public record PersonDto(
  int personId,
  String personName,
  int age,
  String gender
) {}
