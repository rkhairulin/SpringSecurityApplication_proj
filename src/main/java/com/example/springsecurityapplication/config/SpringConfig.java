package com.example.springsecurityapplication.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//данная настройка поможет работать с кастомным валидатором а также создавать его bean
@Configuration
//указываем папки для сканирования
@ComponentScan("com.example.springsecurityapplication.util")
public class SpringConfig {


}
