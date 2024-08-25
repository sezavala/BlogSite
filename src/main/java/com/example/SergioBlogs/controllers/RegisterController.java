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
public class RegisterController {

    @Autowired
    AccountService accountService;

    @GetMapping("/signup")
    public String getRegisterPage(Model model){
        Account account = new Account();
        model.addAttribute("account", account);
        return "signup";
    }

    @PostMapping("/signup")
    public String registerNewUser(@ModelAttribute Account account){
        accountService.save(account);

        return "redirect:/";
    }
}
