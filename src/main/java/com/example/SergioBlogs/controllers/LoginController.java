package com.example.SergioBlogs.controllers;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    AccountService accountService;

    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "login";
    }

    @PostMapping("/login")
    public String logUserIn(@ModelAttribute Account account){
        accountService.save(account);

        return "redirect:/";
    }
}
