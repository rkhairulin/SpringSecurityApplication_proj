package com.example.springsecurityapplication.services;


import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//сервис по работе с моделью
@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;


//    10.1  00:21:10 внедряем PasswordEncoder, позволяет шифровать пароли по BCrypt
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


//   метод для получения пользователя по логину
    public Person getPersonFindByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }


//    метод по регистрации
//    @Transactional перед методом - что бы разрешить запись в базу
    @Transactional
    public void registration(Person person){

//  10.1  00:22:20 - устанавливаем в качестве пароля при регистрации хэш
        person.setPassword(passwordEncoder.encode(person.getPassword()));

//  10.1  00:41:50 назначим пользователю роль ROLE_USER при регистрации
//  ROLE_USER, ROLE_ADMIN, ROLE_ - все роли именуются в данном ключк
        person.setRole("ROLE_USER");

        personRepository.save(person);
    }


    @Transactional
    public void changeUserRole(Person person,String role){
        person.setRole(role);
        personRepository.save(person);
    }
}
