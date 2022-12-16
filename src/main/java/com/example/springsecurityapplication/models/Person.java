package com.example.springsecurityapplication.models;



import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

// Подключаемся к существующей таблице person, связываем класс Person с таблицей person с помощью аннотации @Entity
//@Entity — Указывает, что данный бин (класс) является сущностью. @Table — указывает на имя таблицы, которая будет отображаться в этой сущности. @Column — указывает на имя колонки, которая отображается в свойство сущности.
//
@Entity
// связь с таблицей с конкретным именем
@Table(name = "person")
public class Person {
//    задаем первичный ключ, его стратегию автоинкримента а также колонку в таблице для свчзи через @Column
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "login")
    @NotEmpty(message = "Логин не может быть пустым")
    @Size(min = 5, max = 10, message = "Логин должен быть от 5 до 10 символов включительно")
    private String login;


    @NotEmpty(message = "Пароль не может быть пустым")
//    @Size(min = 6, max = 15, message = "Пароль должен быть от 6 до 15 символов включительно")
    //  проверка с помощью регулярного выражения

    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,60})" , message = "Пароль должен содержать от 6 до 60 символов включительно; все буквы латинские; содержит хотя бы одно число; содержит хотя бы один спецсимвол (@#$%); содержит хотя бы одну латинскую букву в нижнем регистре; содержит хотя бы одну латинскую букву в верхнем регистре")


    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;



//   12.03  00:00:30  таблица для заказов: связь многие ко многим
    @ManyToMany()
//    name = "product_cart" - имя новой таблицы для связи
//      joinColumns,inverseJoinColumns - задают поля новой таблицы ("person_id","product_id"). joinColumns - свое поле, inverseJoinColumns - поле 2й таблицы
//    //    формируется лист объектов своего класса
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id") )
    private List<Product> products;



//  12.03  00:46:30 связи с заказом (Order)
//    1 пользователь во многих заказах, поэтому List
//    (mappedBy = "product") - связь через поле product  модели Order
    @OneToMany(mappedBy = "person")
    private List<Order> orderList;




//    2 конструктора + геттеры/сеттеры:
    public Person() {
    }

//    без id !!!!
    public Person(String login, String password) {
        this.login = login;
        this.password = password;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
