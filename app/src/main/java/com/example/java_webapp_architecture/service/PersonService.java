package com.example.java_webapp_architecture.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.java_webapp_architecture.constant.Const;
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

  /**
   * 登録情報 取得
   * @param int id
   * @return Optional<PersonDto> personDto - 登録情報
   */
  public Optional<PersonDto> getById(int id) {
    return personList.stream()
      .filter(person -> person.personId() == id)
      .findFirst();
  }

  /**
   * 新規登録
   * @param PersonDto person - 登録情報
   */
  public void add(PersonDto person) {
    if (person.personId() == Const.ID_NOT_REGISTERED) {
      int newId = getNewId();

      PersonDto newPerson =
        new PersonDto(newId, person.personName(), person.age(), person.gender());
      
      personList.add(newPerson);
    }
  }

  /**
   * 更新
   * @param PersonDto person - 登録情報
   */
  public void update(PersonDto person) {
    int id = person.personId();

    if (personExists(id)) {
      personList.removeIf(p -> p.personId() == id );
      personList.add(person);
    }
  }

  /**
   * 登録削除
   * @param int id
   */
  public void delete(int id) {
    if (personExists(id)) {
      personList.removeIf(person -> person.personId() == id);
    }
  }

  /**
   * 全件取得
   * @return List<PersonDto> personList - 登録者一覧
   */
  public List<PersonDto> getAll() {
    return personList;
  }

  /**
   * 登録済み / 未登録 確認
   * @param int id - 登録者ID
   * @return　boolean exists - true: 登録済み false: 未登録
   */
  private boolean personExists(int id) {
    return personList.stream().anyMatch(person -> person.personId() == id);
  }

  private int getNewId() {
    List<Integer> idList = personList.stream()
      .map(person -> person.personId())
      .toList();
    
    int newId = 1;
    while (true) {
      if (idList.contains(newId)) {
        newId ++;
      } else {
        break;
      }
    }

    return newId;
  }
}
