package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
//для срабатывания данного контроллера запрос должен начинаться с "/authentication"
@RequestMapping("/authentication")
public class AuthenticationController {

//    9.2  00:57:20
//    внедряем валидатор и сервис
    private final PersonValidator personValidator;
    private final PersonService personService;
    @Autowired
    public AuthenticationController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }



    //    http:localhost:8080/authentication/login
    @GetMapping("/login")
    public String login(){
        return "login";
    }



//    9.2  00:41:30 Прикрутим регистрацию
    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("person", new Person());
        return "registration";
    }



//9.2  00:58:00 принимаем данные с формы
    @PostMapping("/registration")
//    @Valid - форма валидируется, ошибки помещаются в bindingResult
//    BindingResult -интерфейс наследующий  интерфейс ошибок Errors
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){
        personValidator.validate(person, bindingResult);
//        здесь проверяются как ошибки из кастомного валидатора  personValidator.validate(person, bindingResult); так и ошибки из аннотации @Valid Person person
        if(bindingResult.hasErrors()){
            return "registration";
        }
//  если ошибок нет - сохраняем пользователя и запускаем get-запрос на главнуб страницу
        personService.registration(person);
        return "redirect:/index";
    }
}
