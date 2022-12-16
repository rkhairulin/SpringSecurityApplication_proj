package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// <Модель, тип данных id>
public interface PersonRepository extends JpaRepository<Person, Integer>{

//    получаем запись из бд по логину
    Optional<Person> findByLogin(String login);


// получить лист со всеми товарами
    List<Person> findAll();

}
