package com.example.springsecurityapplication.util;

import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


//11.02   01:41:00 Кастомная Валидация
@Component
public class ProductValidator implements Validator {

//  внедрим сервис
    private final ProductService productService;
    @Autowired
    public ProductValidator(ProductService productService) {
        this.productService = productService;
    }


//   имплементируем родительские методы
//    проверка класса предназначения валидатора
    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.equals(clazz);
    }


//    Кастомная Валидация
//    Errors errors - массив под ошибки
    @Override
    public void validate(Object target, Errors errors) {
        Product product = (Product) target;

//        11.02   01:46:00 Кастомная Валидация продолжение
        if(productService.getProductFindByTitle(product) != null){
            errors.rejectValue("title", "","Данное наименование товара уже используется");
        };
    }
}
