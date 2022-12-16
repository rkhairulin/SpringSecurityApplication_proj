package com.example.springsecurityapplication.util;

import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
//!имплементируем именно спринговский интерфейс валидатор
public class PersonValidator implements Validator {

//  9.2  00:53:50
//    внедряем сервис
    private final PersonService personService;
//    у лектора аннотации нет
    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }


//    переопределяем методы имплементируемого интерфейса
    //    в методе указываем модель для которой предназначен данный валидатор(сравниваем Person с классом что приходит в метод)
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

// преобразуем объект в Person
//    на входе валидируемый объект и объект ошибок в который будет положена ошибка
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
//  9.2  00:53:50 - Если метод по поиску пользователя по логину выдает не null, то тогда логин уже занят
        if(personService.getPersonFindByLogin(person) != null){
            errors.rejectValue("login", "", "Логин занят");
        }
    }
}
