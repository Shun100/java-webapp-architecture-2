package com.example.java_webapp_architecture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.java_webapp_architecture.dto.PersonDto;

@Controller
@RequestMapping("/spring_mvc_person")
public class PersonController {
  @Autowired
  private final PersonService personService = new PersonService();

  /**
   * 新規登録画面を開く
   * @param Model model
   * @return String personInputPage - 新規登録画面のHTML
   */
  @GetMapping("/create")
  public String createPerson(@RequestParam int personId, Model model) {
    // PersonDto person = new PersonDto(1, "Alice", 26, "female");
    // model.addAttribute("person", person);
    Option<PersonDto> optionalPerson = personService.getById(personId);
    optionalPerson.ifPresent(person -> model.addAttribute("person", person));
    return "personInputPage";
  }

  @GetMapping("/confirm")
  public String cofirm(Model model) {
    return "";
  }

  @GetMapping("/edit")
  public String edit(Model model) {
    return "";
  }

  @GetMapping("/delete")
  public String delete(Model model) {
    return "";
  }

  @GetMapping("/table")
  public String getTable(Model model) {
    return "personTablePage";
  }




}
