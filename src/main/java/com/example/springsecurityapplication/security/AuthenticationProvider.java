/*
//комментим для 9.2.00:00:00 Встроенная аутенфикауия SpringSecurity

package com.example.springsecurityapplication.security;

import com.example.springsecurityapplication.services.PersonDetailsService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collections;


// сразу помечаем @Component для авто создания бина данного класса
// наследуем от интерфейса AuthenticationProvider
@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {



// 9.1.00:47:30
//  внедряем сервис (тк. внем прописан нужный нам метод по поиску пользователя по логину):
    private final PersonDetailsService personDetailsService;
    // конструктор для инициализации
    @Autowired
    public AuthenticationProvider(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }
//    теперь пропишем логику по идентификации в методе authenticate(



//  переназначаем методы родительского интерфейса:

//  метод определяет случаи применения именно данного AuthenticationProvider
//    указываем true == для всех случаев
@Override
public boolean supports(Class<?> authentication) {
    return true;
}

//  метод с логикой по аутентификации в нашем приложении:
//  Authentication authentication - объект с формы аутентификации
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        получаем логин из объекта аутентификации из формы
//        за нас спринг секьюрити сам возьмет объект из формы и передаст сюда
        String login = authentication.getName();



//      9.1.00:48:00 логика аутентификации
//        объект создается именно через UserDetails т.к. в методе loadUserByUsername мы получаем на выходе именно UserDetails объект
        UserDetails person = personDetailsService.loadUserByUsername(login);
//        получаем пароль с формы аутентификации
        String password = authentication.getCredentials().toString();
//        теперь имея логин и пароль как из базы так и из формы мы можем их сравнить
//        если пароль с формы НЕ равен паролю пользователя из базы найденному по логину, то выбрасываем исключение BadCredentialsException - неверные реквизиты/данные для входа
        if(!password.equals(person.getPassword())){
            throw new BadCredentialsException("Не корректный пароль");
//        иначе,
        }
//        возвращаем объект аутентификации. В нем - пользователь, который аутентифицировался, его пароль, его права доступа
        return new UsernamePasswordAuthenticationToken(person, password, Collections.emptyList());
    }
}
*/
