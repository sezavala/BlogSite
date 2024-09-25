package com.example.SergioBlogs.services;

import com.example.SergioBlogs.models.Authority;
import com.example.SergioBlogs.models.Post;
import com.example.SergioBlogs.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorityService {

    @Autowired
    private PostService postService;
    @Autowired
    private AuthorityRepository authorityRepository;

    // Check if the user has a specific authority
    public boolean hasAuthority(Authentication authentication, String authorityName) {
        Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return authorities.contains(authorityName);
    }

    // Check if the user can edit a specific post
    public boolean canEditPost(Authentication authentication, Long postId) {
        return hasAuthority(authentication, "ROLE_ADMIN") || isPostOwner(authentication, postId);
    }

    // Check if the authenticated user is the owner of the post
    private boolean isPostOwner(Authentication authentication, Long postId) {
        Post post = postService.getById(postId).orElse(null); // Fetch the post
        if (post != null) {
            String email = authentication.getName(); // Assuming email is used as username
            return post.getAccount().getEmail().equals(email); // Check ownership
        }
        return false; // Post not found
    }

    public Optional<Authority> getByName(String name) {
        return authorityRepository.findById(name);
    }

    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }

    public List<Authority> getAll() {
        return authorityRepository.findAll();
    }

    public void delete(String name) {
        authorityRepository.deleteById(name);
    }
}
