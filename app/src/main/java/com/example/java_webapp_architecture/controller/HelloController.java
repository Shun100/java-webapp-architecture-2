package com.example.java_webapp_architecture.controller;

// import org.springframework.web.bind.annotation.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {
  @GetMapping("/hello")
  public String sayHello() {
    return "Hello";
  }
}
