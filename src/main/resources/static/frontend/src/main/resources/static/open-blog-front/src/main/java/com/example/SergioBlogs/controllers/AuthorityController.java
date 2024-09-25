package com.example.SergioBlogs.controllers;

import com.example.SergioBlogs.models.Authority;
import com.example.SergioBlogs.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    // Create a new authority
    @PostMapping
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) {
        // Optionally, check if the authority already exists
        if (authorityService.getByName(authority.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }

        Authority createdAuthority = authorityService.save(authority);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthority); // 201 Created
    }

    // Get all authorities
    @GetMapping
    public ResponseEntity<List<Authority>> getAllAuthorities() {
        List<Authority> authorities = authorityService.getAll();
        return ResponseEntity.ok(authorities); // 200 OK
    }

    // Get authority by name
    @GetMapping("/{name}")
    public ResponseEntity<Authority> getAuthorityByName(@PathVariable String name) {
        Optional<Authority> authority = authorityService.getByName(name);
        return authority.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // Optional: Delete an authority
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable String name) {
        if (authorityService.getByName(name).isPresent()) {
            authorityService.delete(name);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
