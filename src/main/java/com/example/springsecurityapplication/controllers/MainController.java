package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.repositories.ProductRepository;
import com.example.springsecurityapplication.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class MainController {



//    12.02  00:20:22 внедряем репозиторий
    private final ProductRepository productRepository;



    private final ProductService productService;
    @Autowired
    public MainController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

//  список товаров для незарегистрированных
    @GetMapping("")
    public String getAllProduct(Model model){
        model.addAttribute("products", productService.getAllProduct());
        return "product/product";
    }


//  обработка перехода на info/{id} с карточкой товара
    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model){
        model.addAttribute(("productInfo"), productService.getProductId(id));
        return "product/infoProduct";
    }




//    12.01   01:07:00 Обработка формы сортировки
//   @RequestParam(value = "price", required = false, defaultValue = "") - для случаев когда параметр как может придти так может и не придти с формы
    @PostMapping("/search")
    public String indexSearch(@RequestParam("search") String search, @RequestParam("ot") String ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model){



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

        return "product/product";
    }







}
