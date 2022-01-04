package com.bnifp.mufis.categoryservice.controller;

import com.bnifp.mufis.categoryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/posts")
public class CategoryController extends BaseController {
    @Autowired
    @Qualifier("postServiceImpl")
    private CategoryService categoryService;

//    @Autowired
//    RestTemplate restTemplate;

//    @Autowired
//    private KafkaProducerImpl producer;
}
