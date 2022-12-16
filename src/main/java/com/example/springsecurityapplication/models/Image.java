package com.example.springsecurityapplication.models;


import javax.persistence.*;


@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fileName;



//  установим связь с продуктом
//  @ManyToOne  несколько картинок к одному товару
//  fetch = FetchType.EAGER  - если загружается фото то с ней нужно подгрузить и продукт к которому она относится
//  optional = false  -   данная таблица не является родительской
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;



//  пустой конмтруктор + конмтруктор без id
//    гетеры-сетеры

    public Image() {
    }

    public Image(String fileName, Product product) {
        this.fileName = fileName;
        this.product = product;
    }

//    гетеры-сетеры

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
