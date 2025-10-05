package com.example.java_webapp_architecture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.java_webapp_architecture.dto.PersonDto;
import com.example.java_webapp_architecture.service.PersonService;

@Controller
// @RequestMapping("/spring_mvc_person")
public class PersonController {
  @Autowired
  private final PersonService personService = PersonService.getInstance();

  /**
   * 新規登録画面を開く
   * @param Model model
   * @return String personInputPage - 新規登録画面のHTML
   */
  @GetMapping("/create")
  public String createPerson(@RequestParam int personId, Model model) {
    Optional<PersonDto> optionalPerson = personService.getById(personId);
    optionalPerson.ifPresentOrElse(
      person -> model.addAttribute("person", person),
      () -> model.addAttribute("person", new PersonDto()));

    return "personInputPage";
  }

  /**
   * 確認画面を開く
   * @param Model model
   * @return String personUpdatePage - 確認画面
   */
  @PostMapping("/confirm")
  public String confirm(
    @RequestParam("personId") int id,
    @RequestParam("personName") String name,
    @RequestParam("age") int age,
    @RequestParam("gender") String gender,
    Model model) {

      PersonDto personDto = new PersonDto(id, name, age, gender);

      model.addAttribute("person", personDto);

    return "personUpdatePage";
  }

  /**
   * 新規登録
   * @param Model model
   * @return String personTablePage - 登録者一覧画面
   */
  @PostMapping("/update")
  public String edit(
    @RequestParam("personId") int id,
    @RequestParam("personName") String name,
    @RequestParam("age") int age,
    @RequestParam("gender") String gender,
    Model model) {
      PersonDto personDto = new PersonDto(id, name, age, gender);

      if (id == 0) {
        personService.add(personDto);
      } else {
        personService.update(personDto);
      }

      List<PersonDto> personList = personService.getAll();
      model.addAttribute("personList", personList);

    return "personTablePage";
  }

  /**
   * 登録削除
   * @param Model model
   * @return String personTablePage - 登録者一覧画面のHTML
   */
  @PostMapping("/delete")
  public String delete(@RequestParam("personId") int id, Model model) {
    personService.delete(id);

    List<PersonDto> personList = personService.getAll();
    model.addAttribute("personlist", personList);

    return "personTablePage";
  }

  /**
   * 一覧画面
   * @param Model model
   * @return String personTablePage - 登録者一覧画面
   */
  @GetMapping("/table")
  public String getTable(Model model) {
    List<PersonDto> personList = personService.getAll();
    model.addAttribute("personList", personList);
    
    return "personTablePage";
  }

  /**
   * 戻る
   * @param Model model
   * @return String personTablePage - 登録者一覧画面
   */
  @GetMapping("/back")
  public String back(Model model) {
    List<PersonDto> personList = personService.getAll();
    model.addAttribute("personlist", personList);

    return "personTablePage";
  }
}
