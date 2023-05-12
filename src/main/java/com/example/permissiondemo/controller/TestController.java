package com.example.permissiondemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
public class TestController {

    @RolesAllowed("PM")
    @GetMapping("")
    public String test(){
        return "hello";
    }
}
