package com.example.springsecurityapplication.repositories;


import com.example.springsecurityapplication.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//12.03  00:08:00
@Repository
@Transactional
public interface CartRepository extends JpaRepository<Cart, Integer> {

//    метод через JPA Query Creation для нахождения всех заказов по id пользователя
    List<Cart>findByPersonId(int id );



//    12.03  00:33:30 метод cartRepository.delete();
    @Transactional
//   @Modifying - означает что   @Query запрос не должен выдавать результат
    @Modifying
    @Query(value ="DELETE FROM product_cart WHERE product_id=?1 AND person_id=?2", nativeQuery = true)
    void deleteCartById(int product_id, int person_id);
}


//меняем product_id=?1 на id=?1