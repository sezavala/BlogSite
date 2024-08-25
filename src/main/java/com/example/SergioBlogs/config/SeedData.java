package com.example.SergioBlogs.config;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.models.Post;
import com.example.SergioBlogs.services.AccountService;
import com.example.SergioBlogs.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeedData implements CommandLineRunner {
    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception{
        List<Post> posts = postService.getAll();

        if(posts.isEmpty()){

            Account account1 = new Account();
            Account account2 = new Account();

            account1.setFirstName("Sergio");
            account1.setLastName("user");
            account1.setEmail("user.user@domain.com");
            account1.setPassword("password");

            account2.setFirstName("Johanna");
            account2.setLastName("admin");
            account2.setEmail("admin.admin@domain.com");
            account2.setPassword("password");

            accountService.save(account1);
            accountService.save(account2);

            Post post1 = new Post();
            post1.setTitle("Blog On My Girlfriend");
            post1.setBody("Post Body");
            post1.setAccount(account1);

            Post post2 = new Post();
            post2.setTitle("Blog On My Boyfriend");
            post2.setBody("Post2 Body");
            post2.setAccount(account2);

            postService.save(post1);
            postService.save(post2);
        }
    }
}
