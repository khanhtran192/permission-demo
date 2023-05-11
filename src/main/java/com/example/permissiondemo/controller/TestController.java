package com.example.permissiondemo.controller;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RolesAllowed("PM")
    @GetMapping("")
    public String test(){
        return "hello";
    }
}
