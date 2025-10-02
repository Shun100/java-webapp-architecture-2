package com.example.java_webapp_architecture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.java_webapp_architecture.dto.PersonDto;

@Controller
@RequestMapping("/person")
public class PersonController {

  /**
   * 新規登録画面を開く
   * @param Model model
   * @return String personInputPage - 新規登録画面のHTML
   */
  @GetMapping("/create")
  public String createPerson(Model model) {
    PersonDto person = new PersonDto(1, "Alice", 26, "female");
    model.addAttribute("person", person);
    return "personInputPage";
  }
}
