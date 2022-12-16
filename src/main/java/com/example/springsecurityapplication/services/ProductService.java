package com.example.springsecurityapplication.services;


import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//  метод получения списка всех товаров
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }


//  метод получения товара по id
    public Product getProductId(int id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.orElse(null);
    }

//  метод по добавлению товара
    @Transactional
    public void saveProduct(Product product){
        productRepository.save(product);
    }

//  метод по коррекции данных товара
    @Transactional
    public void updateProduct(int id, Product product){
        product.setId(id);
//      (!) назначение новой даты при изменении товара
        product.setDateTimeOfCreation(LocalDateTime.now());
        productRepository.save(product);
    }

//  метод по удалению товара
    @Transactional
    public void deleteProduct(int id){
        productRepository.deleteById(id);
    }


//   11.02 1:43:20 метод получения товара по наименованию
    public Product getProductFindByTitle(Product product){
        Optional<Product> product_db = productRepository.findByTitle(product.getTitle());
        return product_db.orElse(null);
    }

}
