package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Cart;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {

// X_002
    private final ProductRepository productRepository;


//    12.03  00:55:10 внедряем репозиторий заказов
    private final OrderRepository orderRepository;


//12.03  00:16:40 внедрим репозиторий корзины
    private final CartRepository cartRepository;


//    11.02  00:24:20 обработка списка товаров для зарегистрированного пользователя
    private final ProductService productService;
    @Autowired
    public UserController(ProductRepository productRepository, OrderRepository orderRepository, CartRepository cartRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
    }



    @GetMapping ("/index")
    public String index(Model model){


//        9.2  00:28:34 комментируем объект которого у нас пока нет
/*
//        9.1.01:04:00 получаем объект аутенфикации
//        с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутенфикации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();



//        преобразуем объект аутенфикации в класс позволяющий работать с пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//        выведем в консоль объект
        System.out.println(personDetails.getPerson());
//        9.1.01:09:00 выведем в консоль id пользователя, логин и пароль
        System.out.println("ID пользователя: " + personDetails.getPerson().getId());
        System.out.println("Логин пользователя: " + personDetails.getPerson().getLogin());
        System.out.println("Пароль пользователя: " + personDetails.getPerson().getPassword());
*/



//        11.01  00:07:40  ЛК Админа
//        с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутенфикации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//  преобразуем объект аутенфикации в класс позволяющий работать с пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  переменная под роль текущего пользователя
        String role = personDetails.getPerson().getRole();
        if (role.equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }

//    11.02  00:24:20 обработка списка товаров для зарегистрированного пользователя
 //  список товаров для зарегистрированных
        model.addAttribute("products", productService.getAllProduct());

//  user в путь
        return "user/index";
    }




//    11.02  00:24:20 обработка списка товаров для зарегистрированного пользователя
//    по факту копируем функционал незарегистрированного юзера
    //  обработка перехода на info/{id} с карточкой товара
    @GetMapping("/user/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute(("productInfo"), productService.getProductId(id));
        return "user/infoProduct";
    }


/*  ДОДЕЛАТЬ ПО АНАЛОГИИ

    //    12.01   01:07:00 Обработка формы сортировки
//   @RequestParam(value = "price", required = false, defaultValue = "") - для случаев когда параметр как может придти так может и не придти с формы
    @PostMapping("index/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model){

        return "product/product";
    }

*/



//12.03  00:14:00 логика корзины (ссылка - Добавить)
    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
//  с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  преобразуем объект аутенфикации в класс позволяющий работать с текущим пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  извлекаем id текущего пользователя
        int id_person = personDetails.getPerson().getId();
//  формируем объект класса
        Cart cart = new Cart(id_person, id);
//  сохраняем в БД
        cartRepository.save(cart);
        return "redirect:/cart";

    }



//    12.03  00:22:00 визуальное представление корзины
    @GetMapping("/cart")
    public String cart(Model model){
//  с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  преобразуем объект аутенфикации в класс позволяющий работать с текущим пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  извлекаем id текущего пользователя
        int id_person = personDetails.getPerson().getId();
//  лист заказов текущего юзера
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
//  пустой лист под продукты
        List<Product> productList = new ArrayList<>();
//  перебираем все заказы текущего юзера
        for (Cart cart: cartList) {
//  cart.getProductId() - получаем id продукта из корзины
//  по полученному id продукта из корзины методом productService.getProductId получаем соответствующий ему продукт
//  помещаем продукт в лист
        productList.add(productService.getProductId(cart.getProductId()));
        }
//  помещаем лист товаров из карзины текущего юзера в модель для шаблона
        model.addAttribute("cart_product", productList);

//  подсчитаем суммарную стоимость выбраных товаров
    float price = 0;
        for (Product product: productList ) {
            price+=product.getPrice();
        }
//  помещаем сумму товаров в корзине в модель для шаблона
        model.addAttribute("price", price);
        return "user/cart";
    }



//    12.03  00:32:00 Удаление из корзины
    @GetMapping("/cart/delete/{id}")
    public String deleteProductFromCart(@PathVariable("id") int id, Model model){
//  с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  преобразуем объект аутенфикации в класс позволяющий работать с текущим пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  извлекаем id текущего пользователя
        int id_person = personDetails.getPerson().getId();

//  12.03  00:35:00 Удаление из корзины(продолжение)
        cartRepository.deleteCartById(id, id_person);
        return "redirect:/cart";
    }




//12.03  00:50:20 логика нажатия кнопки "Оформить заказ" в корзине
    @GetMapping("/order/create")
    public String createOrder(){
//  с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  преобразуем объект аутенфикации в класс позволяющий работать с текущим пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  извлекаем id текущего пользователя
        int id_person = personDetails.getPerson().getId();

//  лист заказов текущего юзера
        List<Cart> cartList = cartRepository.findByPersonId(id_person);
//  пустой лист под продукты
        List<Product> productList = new ArrayList<>();
//  перебираем все заказы текущего юзера
        for (Cart cart: cartList) {
//  cart.getProductId() - получаем id продукта из корзины
//  по полученному id продукта из корзины методом productService.getProductId получаем соответствующий ему продукт
//  помещаем продукт в лист
            productList.add(productService.getProductId(cart.getProductId()));
        }

//  тут U - "ю" буквы
//  создаем уникальный номер заказа. UUID - неизмененный универсальный уникальный идентификатор файла
        String uuid = UUID.randomUUID().toString();

//  генерируем список заказов
        for (Product product: productList){
//  количество предлагается передавать через js..........................................................................................................................................................................................................................
//  Status.ОФОРМЛЕН - начальный статус заказа
            Order newOrder = new Order(uuid, 1, product.getPrice(), Status.ОФОРМЛЕН,product, personDetails.getPerson());
//  сохраняем объект в бд
            orderRepository.save(newOrder);
//  после добавления в заказ - товар должен удаляться из корзины
            cartRepository.deleteCartById(product.getId(),id_person);
        }
        return "redirect:/orders";
    }


//12.03  00:57:40  метод отображения странички с историйе заказов
    @GetMapping("/orders")
    public String ordersOfUser(Model model){
        //  с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  преобразуем объект аутенфикации в класс позволяющий работать с текущим пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  формируем лист всех заказов текущего пользователя
        List<Order> orderList = orderRepository.findByPerson(personDetails.getPerson());
//  помещаем лист заказов в модель для последующей передачи на шаблон
        model.addAttribute("orders", orderList);
        return "user/orders";
    }



// X_002
//    12.01   01:07:00 Обработка формы сортировки
//   @RequestParam(value = "price", required = false, defaultValue = "") - для случаев когда параметр как может придти так может и не придти с формы
    @PostMapping("/index/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model){
//  12.02  00:20:30 обработка методов сортировки (продолжение)
//  поля от и до заполнены
        if (!ot.isEmpty() & !Do.isEmpty()){
//  выбрана какая-либо сортировка по цене
            if(!price.isEmpty()){
//  выбрана сортировка по возрастанию цены товара
                if(price.equals("sort_by_asc")){
//  выбрана какая-либо категория
                    if(!category.isEmpty()) {
                        if (category.equals("sofa")) {
                            //  search.toLowerCase() - поисковое выражение введенное пользователем в поле "Поиск по ниаменованию" переводим в нижний регистр
                            //  Float.parseFloat(ot) переводим параметр из поля "ot" из строки в float (1вое слово - тип в который конвертируем, далее парсим в него через parseFloat)
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));
                        } else if (category.equals("bed")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));
                        } else if (category.equals("table")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));
                        } else if (category.equals("chair")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 4));
                        }
//  не выбрана ниодна категория
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
//  выбрана сортировка по убыванию цены товара
                }else if(price.equals("sort_by_desc")){
//  выбрана какая-либо категория
                    if(!category.isEmpty()) {
                        if (category.equals("sofa")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));
                        } else if (category.equals("bed")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));
                        } else if (category.equals("table")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));
                        } else if (category.equals("chair")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 4));
                        }
//  не выбрана ни одна категория
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(),Float.parseFloat(ot), Float.parseFloat(Do)));
                    }
                }
//  НЕ выбран тип сортировки по цене
            } else {
//  выбрана какая-либо категория
                if(!category.isEmpty()) {
                    if (category.equals("sofa")) {
                        model.addAttribute("search_product", productRepository.findByTitlePriceCategory(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 1));
                    } else if (category.equals("bed")) {
                        model.addAttribute("search_product", productRepository.findByTitlePriceCategory(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 2));
                    } else if (category.equals("table")) {
                        model.addAttribute("search_product", productRepository.findByTitlePriceCategory(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 3));
                    } else if (category.equals("chair")) {
                        model.addAttribute("search_product", productRepository.findByTitlePriceCategory(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do), 4));
                    }
//  не выбрана ни одна категория
                } else {
                    model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThan(search.toLowerCase(), Float.parseFloat(ot), Float.parseFloat(Do)));
                }
            }
//  поля от и до НЕ заполнены
            //  если заполненно только наименование
        } else {
//  выбрана сортировка по цене
            if(!price.isEmpty()){
//  сортировка по возрастанию
                if(price.equals("sort_by_asc")){
                    if(!category.isEmpty()) {
                        if (category.equals("sofa")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceAsc(search.toLowerCase(), 1));
                        } else if (category.equals("bed")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceAsc(search.toLowerCase(), 2));
                        } else if (category.equals("table")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceAsc(search.toLowerCase(), 3));
                        } else if (category.equals("chair")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceAsc(search.toLowerCase(), 4));
                        }
                    }else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase()));
                    }
//  сортировка по убыванию
                }else if(price.equals("sort_by_desc")){
                    if(!category.isEmpty()) {
                        if (category.equals("sofa")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceDesc(search.toLowerCase(), 1));
                        } else if (category.equals("bed")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceDesc(search.toLowerCase(), 2));
                        } else if (category.equals("table")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceDesc(search.toLowerCase(), 3));
                        } else if (category.equals("chair")) {
                            model.addAttribute("search_product", productRepository.findByTitleCategoryOrderByPriceDesc(search.toLowerCase(), 4));
                        }
                    }else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase()));
                    }
                }
//  НЕ выбрана сортировка по цене
            } else {
                if(!category.isEmpty()){
                    if (category.equals("sofa")) {
                        model.addAttribute("search_product", productRepository.findByTitleCategory(search.toLowerCase(), 1));
                    } else if (category.equals("bed")) {
                        model.addAttribute("search_product", productRepository.findByTitleCategory(search.toLowerCase(), 2));
                    } else if (category.equals("table")) {
                        model.addAttribute("search_product", productRepository.findByTitleCategory(search.toLowerCase(), 3));
                    } else if (category.equals("chair")) {
                        model.addAttribute("search_product", productRepository.findByTitleCategory(search.toLowerCase(), 4));
                    }
                }else{
                    model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
                }
            }
        }
//  12.02  00:38:00  при перезагрузке поля сортировок не очищаются
        model.addAttribute("value_search", search);
        model.addAttribute("value_price_ot", ot);
        model.addAttribute("value_price_do", Do);
//  строчка ниже нужна чтобы полный список продуктов и далее присутствовал на страничке но уже ниже результатов поиска
        model.addAttribute("products", productService.getAllProduct());
        return "user/index";
    }



}
