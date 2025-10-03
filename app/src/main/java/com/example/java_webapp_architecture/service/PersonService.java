package com.example.java_webapp_architecture.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.java_webapp_architecture.dto.PersonDto;

@Service
public class PersonService {
  private static PersonService personService;
  private static List<PersonDto> personList = new ArrayList<>();

  private PersonService() {}

  /**
   * PersonServiceをシングルトンにするために、Factoryメソッドで同じインスタンスを使い回す。
   * @return PersonService personServce
   */
  public static PersonService getInstance() {
    if (personService == null) {
      personService = new PersonService();
    }
    return personService;
  }

  public Optional<PersonDto> getById(int id) {
    return personList.stream()
      .filter(person -> person.personId() == id)
      .findFirst();
  }

  public void add(PersonDto person) {
    if (!personExists(person.personId())) {
      personList.add(person);
    }
  }

  public void update(PersonDto person) {
    int id = person.personId();

    if (personExists(id)) {
      personList.removeIf(p -> p.personId() == id );
      personList.add(person);
    }
  }

  private boolean personExists(int id) {
    return personList.stream().anyMatch(person -> person.personId() == id);
  }
}
