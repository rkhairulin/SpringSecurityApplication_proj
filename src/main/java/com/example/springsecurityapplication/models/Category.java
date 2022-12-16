package com.example.springsecurityapplication.models;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


//12.01   00:08:40 Категории товаров
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;



//    12.01   00:10:40 Категории товаров связь с Product
//  данная страница по отношению к картинкам родительская
//  лист тк одна категория ко многим товарам
//  mappedBy = "category" - поле для связи 2х таблиц
    @OneToMany(mappedBy = "category")
    private List<Product> product;



//    пустой конмтруктор + конмтруктор без id
//    гетеры-сетеры

    public Category() {
    }

    public Category(String name, List<Product> product) {
        this.name = name;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
