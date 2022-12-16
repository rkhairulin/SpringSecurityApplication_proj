package com.example.springsecurityapplication.repositories;

import com.example.springsecurityapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

// 11.02 01:42:50 создадим метод findByTitle() для поиска продукта по наименованию
Optional<Product> findByTitle(String title);



//  12.02  00:02:20  методЫ репозитория для сортировки поиска и фильтрации
// поиск по части наименования товара не взирая на регистр
// 1) поиск по ключевому слову (части слова)
List<Product> findByTitleContainingIgnoreCase(String name);



//поиск по части наименования товара и фильтрация по диапазону цен
// 2) sql запрос
// (lower(title) - переводит наименование товара в нижний регистр
 // LIKE'%?1%'  - 1й параметр из запроса (это String title) находится в названии товара медду 2мя кусками текста
// LIKE'?1%'  - 1й параметр из запроса (это String title) находится в названии товара вначале
 // LIKE'%?1' - 1й параметр из запроса (это String title) находится в названии товара  вконце
// через ПКМ подключаем SQL dialect чтобы убрать выделение желтым

@Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price>=?2 AND price<=?3)", nativeQuery = true)
List<Product> findByTitleAndPriceGreaterThanEqualAndPriceLessThan(String title, float ot, float Do);



//3)поиск по части наименования товара и фильтрация по диапазону цен c сортировкой по возрастанию
// ASC прописывать не обязательно, это вариант сортировки по умолчению
 @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%' ) OR (lower(title) LIKE '%?1')) AND (price>=?2 AND price<=?3) ORDER BY price ASC", nativeQuery = true)
 List<Product> findByTitleOrderByPriceAsc(String title, float ot, float Do);


 //4)поиск по части наименования товара и фильтрация по диапазону цен c сортировкой по убыванию
 @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price>=?2 AND price<=?3) ORDER BY price DESC", nativeQuery = true)
 List<Product> findByTitleOrderByPriceDesc(String title, float ot, float Do);


 //5)поиск по части наименования товара и фильтрация по диапазону цен c сортировкой по возрастанию и фильтрацией по категориям
 @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1% ) OR (lower(title) LIKE '?1%' ) OR (lower(title) LIKE '%?1' )) AND (price >= ?2 AND price <= ?3) AND category_id=?4 ORDER BY price", nativeQuery = true)
 List<Product> findByTitleAndCategoryOrderByPriceAsc(String title, float ot, float Do, int category);


 //6)поиск по части наименования товара и фильтрация по диапазону цен c сортировкой по убыванию и фильтрацией по категориям
 @Query(value = "SELECT* FROM product WHERE category_id=?4 AND ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) AND (price >= ?2 AND price <= ?3) ORDER BY price DESC", nativeQuery = true)
 List<Product> findByTitleAndCategoryOrderByPriceDesc(String title, float ot, float Do, int category);



// 7) имя_диапазон__категория

 @Query(value = "SELECT * FROM product WHERE ((lower(title) LIKE %?1% ) OR (lower(title) LIKE '?1%' ) OR (lower(title) LIKE '%?1' )) AND (price >= ?2 AND price <= ?3) AND category_id=?4", nativeQuery = true)
 List<Product> findByTitlePriceCategory(String title, float ot, float Do, int category);




// 8) имя_возрастание_категория
@Query(value = "SELECT* FROM product WHERE category_id=?2 AND ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) ORDER BY price ASC", nativeQuery = true)
List<Product> findByTitleCategoryOrderByPriceAsc(String title, int category);

// 9) имя_возрастание
@Query(value = "SELECT* FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) ORDER BY price ASC", nativeQuery = true)
List<Product> findByTitleOrderByPriceAsc(String title);

// 10) имя_убывание_категория
@Query(value = "SELECT* FROM product WHERE category_id=?2 AND ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) ORDER BY price DESC", nativeQuery = true)
List<Product> findByTitleCategoryOrderByPriceDesc(String title, int category);

// 11) имя_убывание
@Query(value = "SELECT* FROM product WHERE ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1')) ORDER BY price DESC", nativeQuery = true)
List<Product> findByTitleOrderByPriceDesc(String title);

// 12)имя_категория
@Query(value = "SELECT* FROM product WHERE category_id=?2 AND ((lower(title) LIKE %?1%) OR (lower(title) LIKE '?1%') OR (lower(title) LIKE '%?1'))", nativeQuery = true)
List<Product> findByTitleCategory(String title, int category);


}
