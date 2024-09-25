package com.example.SergioBlogs.services;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.models.Post;
import com.example.SergioBlogs.repositories.AccountRepository;
import com.example.SergioBlogs.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostsByAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new RuntimeException("Account not found with id: " + accountId);
        }

        List<Post> posts = postRepository.findByAccount_Id(accountId);
        return posts;
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        LocalDateTime now = LocalDateTime.now();

        if (post.getId() == null) {
            post.setCreatedAt(now);
        }
        post.setModifiedAt(now);

        return postRepository.save(post);
    }

    public void delete(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.delete(post.get());
        } else {
            // Optionally handle the case where the post is not found
            throw new RuntimeException("Post not found with id: " + id);
        }
    }
}
