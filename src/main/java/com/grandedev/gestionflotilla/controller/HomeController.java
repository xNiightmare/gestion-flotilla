package com.grandedev.gestionflotilla.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    //@GetMapping(value = {"/home", "/"})
    @GetMapping
    public String home(){
        return "Hola, Mundo!!!";
    }

    
}
