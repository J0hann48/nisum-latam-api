package com.nisum.nisumapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1")
public class UserController {

    @GetMapping("/info")
    public String hello(){
        return "Hello";
    }
}