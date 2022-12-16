package com.example.springsecurityapplication.models;

import com.example.springsecurityapplication.enumm.Status;
import javax.persistence.*;
import java.time.LocalDateTime;

//12.03  00:41:50 модель заказа
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


// номер заказа
    private String number;

//  количество товара в заказе
    private int count;

// цена заказа
    private float price;

//дата и время
    private LocalDateTime dateTime;




//    12.03  00:44:50 модель заказа (продолжение)
    private Status status;

//  связи с продуктом и пользователем (кто и что заказал)
    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Person person;





    //  метод при создании объекта класса внесет в него текущую дату и время
    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }


//    2 конструктора+ геттеры/сеттеры
    public Order() {
    }

//  без даты и id
    public Order(String number, int count, float price, Status status, Product product, Person person) {
        this.number = number;
        this.count = count;
        this.price = price;
        this.status = status;
        this.product = product;
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
