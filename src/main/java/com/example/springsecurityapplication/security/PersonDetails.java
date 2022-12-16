package com.example.springsecurityapplication.security;

import com.example.springsecurityapplication.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;


//    В классе PersonDetails находиться детальная информация о пользователе. Можно посмотреть информацию об аккаунте пользователя. Данный класс реализует интерфейс UserDetails
public class PersonDetails implements UserDetails {

//    внедряем нашу модель в данный класс
    private final Person person;

    @Autowired
    public PersonDetails(Person person) {
        this.person = person;
    }

//    при наследовании интерфейса имплиментируются все его методы:
//    а далее их переопределить



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//  10.1  00:47:40 пропишем в данном методе логику по работе с ролями
//  java.util.Collections;
//  создаем коллекцию, указываем что в ней будет лежать лист из одного эл-та(по текущей логике у пользователя одна роль), и в нем пеередаем поль пользователя
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }



// метод позволяет получить пароль пользователя
    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

// метод позволяет получить логин пользователя
    @Override
    public String getUsername() {
        return this.person.getLogin();
    }


//    далее все в true для простоты
//    аккаунт действиелен
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

//    аккаунт НЕ заблокирован
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

//    реквизиты для входа не истекли, валидны, де   ствительны
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
//аккаунт активен
    @Override
    public boolean isEnabled() {
        return true;
    }

// создадим етод для получения пользователя
    public Person getPerson(){
        return this.person;
    }

}
