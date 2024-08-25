package com.example.SergioBlogs.controllers;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.models.Post;
import com.example.SergioBlogs.services.AccountService;
import com.example.SergioBlogs.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model){
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()){
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else{
            return "404";
        }
    }

    @GetMapping("/post/new")
    public String newPost(Model model){
        Optional<Account> optionalAccount = accountService.findByEmail("user.user@domain.com");
        if (optionalAccount.isPresent()){
            Post post = new Post();
            post.setAccount(optionalAccount.get());
            model.addAttribute("post", post);
            return "new_post";
        } else{
            return "redirect:/login";
        }
    }

    @PostMapping("/post/new")
    public String saveNewPost(@ModelAttribute Post post){
        postService.save(post);
        return "redirect:/posts/" + post.getId();
    }
}
