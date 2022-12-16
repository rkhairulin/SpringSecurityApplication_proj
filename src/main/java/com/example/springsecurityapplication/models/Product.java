package com.example.springsecurityapplication.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Entity - аннотация модели
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

   @Column(name="title", nullable=false, columnDefinition = "text", unique = true)
   @NotEmpty(message = "Наимменование товара не может быть пустым")
   private String title;

    @Column(name="description", nullable=false, columnDefinition = "text")
    @NotEmpty(message = "Описание товара не может быть пустым")
    private String description;

//   @NotEmpty - строки,  @NotNull - числа
    @Min(value = 100, message = "Цена не может быть ниже 100 руб.")
    @Column(name="price", nullable=false, columnDefinition = "float")
    @NotNull(message = "Поле Цена не может быть пустым")
    private float price;

    @Column(name="storage", nullable=false, columnDefinition = "text")
    @NotEmpty(message = "Поле Склад не может быть пустым")
    private String storage;

    @Column(name="seller", nullable=false, columnDefinition = "text")
    @NotEmpty(message = "Поле Продавец не может быть пустым")
    private String seller;



//  11.02  00:34:10 связь с картинками
//  данная страница по отношению к картинкам родительская
//  @OneToMany у одного продукта - несколько фото
//  cascade = CascadeType.ALL - при удалении продукта автоматически удаляются все его фото из БД
//  fetch = FetchType.LAZY - если подгружается товар то не обязательно сразу подгружать фото
//  mappedBy = "product" - поле для связи
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
//  тк по одному товару несколько фото - то создаем лист под каждый товар
    private List<Image> imageList = new ArrayList<>();



//12.01   00:09:50 Категории товаров связь с Product
// //  optional = false  - данная таблица по отношению к категориям дочерняя
    @ManyToOne(optional = false)
    private Category category;



//   12.03  00:03:40  таблица для заказов: связь многие ко многим
    @ManyToMany()
//    name = "product_cart" - имя новой таблицы для связи
//      joinColumns,inverseJoinColumns - задают поля новой таблицы ("person_id","product_id"). joinColumns - свое поле, inverseJoinColumns - поле 2й таблицы
//    формируется лист объектов своего класса
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id") )
    private List<Person> personList;



//  12.03  00:45:40 связи с заказом (Order)
//    1 продукт во многих заказах, поэтому List
//    (mappedBy = "product") - связь через поле product модели Order
    @OneToMany(mappedBy = "product")
    private List<Order> orderList;




// 11.02  00:37:40 дата (дата и время создания товара)
    private LocalDateTime dateTimeOfCreation;
//  по сути ниже метод по аналогии методов спринг кор, сробатывающих при инициализации бина (при создании объекта класса)
//  при создании объекта продукта будет заполнятся дата создания товара
    @PrePersist
    private void init(){
        dateTimeOfCreation = LocalDateTime.now();
    }



//11.02  01:01:30 метод по добавлению картинок в созданый нами лист в классе Image
    public void addImageProduct(Image image){
        image.setProduct(this);
        imageList.add(image);
    }




    public Product() {
    }

//   без id
    public Product(String title, String description, float price, String storage, String seller) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.storage = storage;
        this.seller = seller;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }



// 11.02  00:39:30  добавим для getter/setter для картинки и даты
 //12.01   00:10:40     добавим для getter/setter для категорий

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public LocalDateTime getDateTimeOfCreation() {
        return dateTimeOfCreation;
    }

    public Category getCategory() {
        return category;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDateTimeOfCreation(LocalDateTime dateTimeOfCreation) {
        this.dateTimeOfCreation = dateTimeOfCreation;
    }
}
