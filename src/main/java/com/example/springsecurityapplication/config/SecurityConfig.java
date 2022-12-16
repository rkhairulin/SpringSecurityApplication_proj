package com.example.springsecurityapplication.config;


import com.example.springsecurityapplication.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

// @EnableWebSecurity - пометка класса для конфигурирования модуля безопасности
// всегда наследует класс WebSecurityConfiguration
//Это основной конфиг файл для конфигурации безопасности в нашем приложении
@EnableWebSecurity



//  10.1  00:58:13   Способ№2_Разграничение ролей с помощью аннотаций: добавляем аннотацию разрешающую в приложении разграничение ролей на основа аннотаций
//@EnableGlobalMethodSecurity(prePostEnabled = true)



//public class SecurityConfig extends WebSecurityConfiguration{

//9.2   00:17:00 для своего варианта аутентификации используется WebSecurityConfigurerAdapter (строчку выше комментируем)
//то что он перечеркнут - значит что есть его новый аналог
public class SecurityConfig extends WebSecurityConfigurerAdapter {
// 9.2  00:00:00 внедряем сервис
private final PersonDetailsService personDetailsService;
    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }



// 9.2  00:00:00
//    метод по настройке аутентификации
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        аутентификация с вомощью сервиса userDetailsService (делигируем все спринг секюрити)
        authenticationManagerBuilder.userDetailsService(personDetailsService)

//  10.1  00:25:00 добавим хэширование при аутентификации
        .passwordEncoder(getPasswordEncoder());


    }



//9.2  00:18:00 создадим еще один метод по настройке аутентификации
//    конфигурируем авторизацию в Spring Security
    @Override
    protected void configure(HttpSecurity http) throws Exception{
//        по умолчанию при переопределении формы аутентификации ВСЕ страницы становятся доступными, так настроен Spring Security,
//        модуль разграничения доступа

        http

//      9.2 01:27:00  отключает csrf защиту от межсайтовой подделки запросов! Важный модуль!
//        .csrf().disable()

//      Указываем что ВСЕ страницы защищены аутентификацией
            .authorizeRequests()
//      указаные ниже страницы доступны ВСЕМ пользователям в том числе и не аутентифицированным
//  11.02  00:09:00 добавим свободный доступ к /product
//  12.01  01:20:00 добавим свободный доступ к "/product/search"
                .antMatchers("/authentication/login", "/error", "/authentication/registration", "/product","/product/info/{id}", "/img/**", "/product/search").permitAll()



//  10.1  00:52:20 Способ№1_Разграничение ролей с помощью файла конфигурации
//      комментим строки ниже и прописываем логику ролей
//      указываем чтодля всех остальных страниц неаутентифицированные пользователи отправляются в метод аутентификации
//      .anyRequest().authenticated()

//      и прописываем логику ролей
//      Spring Security сам отбрасывает часть ROLE_
//      "/admin" - только для админа, все остальные страницы для всех ролей

//  10.1  00:58:00 Способ№2_Разграничение ролей с помощью аннотаций
//      комментируем строку с правами админа ниже
//      закоментим для 11.01  00:17:50
//            .antMatchers("/admin").hasAnyRole("ADMIN")


            .anyRequest().hasAnyRole("USER","ADMIN")



//      для разделения модулей конфигурации используется .and()
            .and()
//      далее модуль конфигурвции аутентификации
//        указываем url запрос который отправляется при попытке зайти на закрытые для пользователя страницы

//        комментим для 9.2  00:36:00
//        http.formLogin().loginPage("/authentication/login")

          .formLogin().loginPage("/authentication/login")
//        указываем url на который будут отправляться данный с формы. При этом не нужно будет создаватье метод в контроллере и обрабатывать данные с формы. Мы задаем url по умолчанию, который позволяет обрабатывать форму аутентификации в Spring Security. Spring Security будет ждать логин и пароль с формы и затем серит их с данными в БД.
//       "/process_login" - тот же url что мы указали ранее в форме, это url по умолчаню
          .loginProcessingUrl("/process_login")
//       указываем на какой url необходимо направить пользователя после успешной аутентификации. true - означает что перенаправление идет в любом случае при успешной аутентификации
          .defaultSuccessUrl("/index", true)
//      url перенаправления в случае НЕ успешной аутентификации, в url будет передан объект, данный объект будем проверять на форме, если он есть - выводить сообщение "Неверный логин или пароль"
          .failureUrl("/authentication/login?error")

//  9.2  01:14:50 - организуем возможность выхода из ЛК (logout)
//      добавляем новый модуль
          .and()
//      при переходе на "/logout" будет очищена сессия пользователя и произойдет перенаправления на "/authentication/login"
          .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication/login");
    }





// 9.2   00:00:00
//    временно отключемк шифрование паролей (бин шифрования паролей)
//    перечеркивание говорит что это не рекомендуется
    @Bean
public PasswordEncoder getPasswordEncoder(){
//        return NoOpPasswordEncoder.getInstance();

//        10.1  00:19:50 Реализация шифрования паролей на практике
//        включаем шифрование паролей указывая метод шифрования
        return new BCryptPasswordEncoder();
    }


/*
//9.1.00:55:50
//    внедрим объект аутентификации с прописанной в нем логикой
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

//    метод по настройке аутентификации
//    в нем мы указываем что аутентификация будет проходить по логике описанной в AuthenticationProvider
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }
*/



//  Х008 снимаем защиту с папок("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icons/**");

//     throws Exception

    @Override
    public void configure(WebSecurity web){
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/imges/**", "/icons/**");
    }




}
