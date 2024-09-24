package com.example.SergioBlogs.controllers;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.models.Post;
import com.example.SergioBlogs.services.AccountService;
import com.example.SergioBlogs.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getSinglePost(@PathVariable Long id) {
        return postService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Account> account = accountService.findByEmail(email);
            account.ifPresent(post::setAccount);

            post.setCreatedAt(LocalDateTime.now());
            post.setModifiedAt(LocalDateTime.now());
            Post created = postService.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Post> optionalPost = postService.getById(id);
            if (optionalPost.isPresent()) {
                Post existingPost = optionalPost.get();

                // Ensure the authenticated user is the owner of the post
                String email = authentication.getName();
                Optional<Account> account = accountService.findByEmail(email);
                if (account.isPresent() && existingPost.getAccount().getId().equals(account.get().getId())) {
                    existingPost.setTitle(updatedPost.getTitle());
                    existingPost.setBody(updatedPost.getBody());
                    existingPost.setModifiedAt(LocalDateTime.now());
                    Post savedPost = postService.save(existingPost);
                    return ResponseEntity.ok(savedPost);
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
                }
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Post> post = postService.getById(id);
            if (post.isPresent()) {
                // Ensure the authenticated user is the owner of the post
                String email = authentication.getName();
                Optional<Account> account = accountService.findByEmail(email);
                if (account.isPresent() && post.get().getAccount().getId().equals(account.get().getId())) {
                    postService.delete(id);
                    return ResponseEntity.noContent().build(); // 204 No Content
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
                }
            } else {
                return ResponseEntity.notFound().build(); // 404 Not Found
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }
    }

    @GetMapping("/author/{accountId}")
    public ResponseEntity<List<Post>> getPostsByAuthor(@PathVariable Long accountId) {
        List<Post> posts = postService.getPostsByAccount(accountId);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(posts); // 200 OK
    }
}
