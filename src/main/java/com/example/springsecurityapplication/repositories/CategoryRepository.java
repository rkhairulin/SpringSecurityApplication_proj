package com.example.springsecurityapplication.repositories;


import com.example.springsecurityapplication.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



//12.01   00:16:50 репозиторий под категории
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

//  метод поиска категории по ее наименованию
    Category findByName(String name);
}
