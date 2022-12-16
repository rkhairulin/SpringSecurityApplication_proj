package com.example.springsecurityapplication.services;


import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.repositories.PersonRepository;

import com.example.springsecurityapplication.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

//сервис по работе аутентификации
@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

// имплементируем и переназначим методу родительского интерфейса
// получаем пользователя по логину для дальнейшей аутентификации
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Option -хранилище с 2мя вариантами: что-то или none
//        получаем пользователя из таблицы person по логину с формы аутентификации
        Optional<Person> person = personRepository.findByLogin(username);
//        если пользователь пустой, не найден
        if(person.isEmpty()){
//            спринг секьюрити поймает данное исключение и сообщение будет выведено на страницу
            throw new UsernameNotFoundException("Пользователь не найден");
        }else{
//          возвращаем объект PersonDetails с аргументом пользователя из Optional
            return new PersonDetails(person.get());
        }
    }
}
