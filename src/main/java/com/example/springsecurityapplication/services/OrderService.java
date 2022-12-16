package com.example.springsecurityapplication.services;


import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import com.example.springsecurityapplication.enumm.Status;

//X_003.05.создаем сервис + метод по  обновлению статуса заказа в БД
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

//  метод по коррекции статуса заказа
    @Transactional
    public void updateOrderStatus(Status status, Order order){
        order.setStatus(status);
        order.setDateTime(LocalDateTime.now());
        orderRepository.save(order);
    }
}






