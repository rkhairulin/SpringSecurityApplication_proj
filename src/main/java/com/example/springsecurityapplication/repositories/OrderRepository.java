package com.example.springsecurityapplication.repositories;


import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

//12.03  00:48:10 репозиторий
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

//    находит все заказы по объекту пользователя
    List<Order> findByPerson(Person person);


//X_003.04.метод извлечения всех заказов из БД
    List<Order> findAll();


//  метод для нахождения заказов по последним символам номенане взирая на регистр
    @Query(value = "SELECT * FROM orders WHERE number LIKE %?1", nativeQuery = true)
    List<Order> findByLastFourSymbols(String phrase);




}
