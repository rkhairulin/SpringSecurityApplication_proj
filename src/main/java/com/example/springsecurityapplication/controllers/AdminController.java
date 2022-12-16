package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.enumm.Status;
import com.example.springsecurityapplication.models.Image;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CategoryRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.repositories.PersonRepository;
import com.example.springsecurityapplication.security.PersonDetails;
import com.example.springsecurityapplication.services.OrderService;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.util.ProductValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


//git test1



//    10.1  00:59:50    данную аннотацию можно навесьть на весь контроллер сразу при необходимости но метод внутри моменяется на hasAnyAuthority
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")



//10.1  00:51:20 пропишем базу под логику админа
@Controller
@RequestMapping("/admin")
public class AdminController {

//  11.02  00:51:00 организуем загрузку фото через форму
//  пропишем путь под папку с фото
    @Value("${upload.path}")
    private String uploadPath;



//11.02   01:49:30 внедрение валидатора
    private final ProductValidator productValidator;


//  X_003.06.внедряем OrderRepository, OrderService
    private final OrderRepository orderRepository;
    private final OrderService orderService;


//  X_005
    private final PersonRepository personRepository;
    private final PersonService personService;






//12.01 00:20:30 внедрим репозиторий для категорий
    private final CategoryRepository categoryRepository;




//11.01  00:47:00 логика по работе с товарами
    private final ProductService productService;
    @Autowired
    public AdminController(ProductValidator productValidator, OrderRepository orderRepository, OrderService orderService, PersonRepository personRepository, PersonService personService, CategoryRepository categoryRepository, ProductService productService) {
        this.productValidator = productValidator;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.personRepository = personRepository;
        this.personService = personService;
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }



//    10.1  00:58:50 аннотация прав доступа
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    @PreAuthorize("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
//    данную аннотацию можно навесьть на весь контроллер сразу при необходимости



//  лк администратора + список товаров
    @GetMapping()
//11.01  00:47:00 логика по работе с товарами
//  для вывода товаров укажем во входящих модель
    public String admin(Model model){

//  11.01  00:17:50 скорректируем логику контроллера админа чтобы юзера при попытке перейти в /admin перебрасывало в лк юзера
//   с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутенфикации. По сути из потока для текущего пользователя мы получаем объект, который был положен в сессию после аутенфикации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//  преобразуем объект аутенфикации в класс позволяющий работать с пользователем
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
//  переменная под роль текущего пользователя
        String role = personDetails.getPerson().getRole();
        if (role.equals("ROLE_USER")){
//  redirect - чтобы адрес в строке был правильный а не /admin
            return "redirect:/index";
        }


//  11.01  00:47:00 логика по работе с товарами
//  помещаем в модель весь список товаров
        model.addAttribute("products", productService.getAllProduct());


        return "admin/admin";
    }


//11.01  00:55:20 обработаем ссылку добавления товаров
//  метод по отображению формы добавлени товара
    @GetMapping("/product/add")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());



// 12.01 00:20:30  положим все наши категории в модель контроллера при добавлении и редактировании (вновь пропускаем работу с сервисным слоем в целях оптимизации времени и напрямую работаем с репозиторием)
//  получаем лист всех значений поля
        model.addAttribute("category", categoryRepository.findAll());




        return "product/addProduct";
    }


//  11.01  01:18:00 обработка формы добавления товара
//   метод по добавлению объекта с формы в таблицу бд

//11.02  00:52:00 организуем загрузку фото через форму / скорректируем метод добавления, добавив MultipartFile
// 11.02   01:34:40  для валидации добавим:  1)аннотация   "@Valid" Product product, 2)"BindingResult bindingResult"

    @PostMapping("/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file_one")MultipartFile file_one, @RequestParam("file_two")MultipartFile file_two, @RequestParam("file_three")MultipartFile file_three, @RequestParam("file_four")MultipartFile file_four, @RequestParam("file_five")MultipartFile file_five, @RequestParam("file_six")MultipartFile file_six) throws IOException {


//11.02   01:49:50 использование валидатора в методе post обработки формы
//  вызываем метод кастомной валидации , передаем в него объект товара и объект для ошибок bindingResult
    productValidator.validate(product, bindingResult);




// 11.02   01:34:40  для валидации добавим: 3)
    if(bindingResult.hasErrors()){
        return "product/addProduct";
    }




//11.02  00:54:00 логика по загрузке фото
//  проверка на пустоту
        if(file_one != null){
//  объект по хранению пути сохранения
            File uploadDir = new File(uploadPath);
//  если путь не существует - его создаем(директорию)
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
//  тут U - "ю" буквы
//  создаем уникальное имя файла. UUID - неизмененный универсальный уникальный идентификатор файла
//   file_one.getOriginalFilename() - наименование файла с формы
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_one.getOriginalFilename();
//  тут throws IOException в титул метода
//  загружаем файл по указаному пути
            file_one.transferTo(new File(uploadPath + "/" + resultFileName));

            Image image = new Image();
//  в качестве продукта - продукт из формы
            image.setProduct(product);
//  в качестве имени - сгенерированное уникальное имя
            image.setFileName(resultFileName);
//  11.02  01:05:00  добавляем картинку в лист картинок данного товара
            product.addImageProduct(image);
        }


//11.02  01:05:00  повторяем для каждого фото
        if(file_two != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_two.getOriginalFilename();
            file_two.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);
        }
        if(file_three != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_three.getOriginalFilename();
            file_three.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);
        }
        if(file_four != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_four.getOriginalFilename();
            file_four.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);
        }
        if(file_five != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_five.getOriginalFilename();
            file_five.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);
        }
        if(file_six != null){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file_six.getOriginalFilename();
            file_six.transferTo(new File(uploadPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageProduct(image);
        }





        productService.saveProduct(product);
        return "redirect:/admin";
    }

//11.01  01:24:50 удаление товара по id
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id){
        productService.deleteProduct(id);
        return "redirect:/admin";
    }

//  11.02  00:00:00  редактирование товара по id
    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model){
        model.addAttribute("editedProduct", productService.getProductId(id));


// 12.01 00:20:30  положим все наши категории в модель контроллера при добавлении и редактировании (вновь пропускаем работу с сервисным слоем в целях оптимизации времени и напрямую работаем с репозиторием)
//  получаем лист всех значений поля
        model.addAttribute("category", categoryRepository.findAll());





        return "product/editProduct";
    }

//  11.02  00:04:15  обработка формы редактирования
    @PostMapping("/product/edit/{id}")
    public String editProduct(@ModelAttribute("editedProduct") Product product, @PathVariable("id") int id){
        productService.updateProduct(id, product);
        return "redirect:/admin";
    }





//  X_003.07.метод обработки ссылки перехода на страницу для работы администраторас заказами
    @GetMapping("/orders")
    public String adminListOfOrders(Model model, Status status){
        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        model.addAttribute("status", status.values());
        return "admin/orders";
    }



//  X_003.08. POST метод обработки изменения статуса заказа (!)
    @PostMapping("/orders/status_change")
    public String adminStatusChange(@RequestParam("status") List<Status> newStatus, Model model,Status status){
        List<Order> orderList = orderRepository.findAll();
        for (int i = 0; i<newStatus.size(); i++){
             if (!orderList.get(i).getStatus().equals(newStatus.get(i))){
                orderService.updateOrderStatus(newStatus.get(i),orderList.get(i));
            }
        }
        List<Order> orderList1 = orderRepository.findAll();
        model.addAttribute("orders", orderList1);
        model.addAttribute("status", status.values());
        return "redirect:/admin/orders";
    }







//    X_004 SQL поиск по последнимчетырем символам номера заказа
    @PostMapping("/orders/search")
    public String adminSearchOrdersByLastFourSymbols(Model model, @RequestParam("searchPhrase") String searchingPhrase){

        if (searchingPhrase.length()<4){
            searchingPhrase += "????";
        }
        String searchingPartOfNumber  = searchingPhrase.trim().substring(0,4);
        List<Order> searchingOrders = orderRepository.findByLastFourSymbols(searchingPartOfNumber);

        model.addAttribute("searchingOrders", searchingOrders);
        model.addAttribute("value_search", searchingPartOfNumber);

        List<Order> orderList = orderRepository.findAll();
        model.addAttribute("orders", orderList);
        return "admin/orders";
    }




    //    X_005 функционал администратора по просмотру информации о пользователях
    @GetMapping("/users")
    public String adminUserManagement(Model model){
        List<Person> personList = personRepository.findAll();
        model.addAttribute("users", personList);
        return "admin/users";
    }


    @PostMapping("/users/change_role")
    public String adminChangeUserRole(@RequestParam("role") List<String> roleList, Model model){
        List<Person> personList = personRepository.findAll();
        for (int i = 0; i<roleList.size(); i++){
            if (!personList.get(i).getRole().equals(roleList.get(i))){
                personService.changeUserRole(personList.get(i),roleList.get(i));
            }
        }
        List<Person> personList1 = personRepository.findAll();
        model.addAttribute("users", personList1);
        return "redirect:/admin/users";
    }



}
