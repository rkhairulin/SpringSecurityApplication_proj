package com.example.springsecurityapplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {
//  внедряем путь к папке для загрузки фото прописаный ранее в application.properties
    @Value("${upload.path}")
    private String uploadPath;


//    GoTo... переопределить метод:
//  метод для раздачи ресурсов
//   если в пути к файлу прописано /img/"какой-то текст" то извлекаем это из дирректории uploadPath
//  "file://" - дирректория в рамках проекта , uploadPath - путь к картинкам
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file://" + uploadPath + "/");
    }
}
