package com.example.springsecurityapplication.models;

import javax.persistence.*;

@Entity
// связь с таблицей с конкретным именем
@Table(name = "product_cart")
public class Cart {
    //    задаем первичный ключ, его стратегию автоинкримента
    //    колонку в таблице для связи @Column(name = "id") НЕ задаем
    @Id
//    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "person_id")
    private int personId;

    @Column(name = "product_id")
    private int productId;



    public Cart() {
    }

    public Cart(int personId, int productId) {
        this.personId = personId;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
