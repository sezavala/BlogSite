package com.example.SergioBlogs.controllers;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if(accountService.findByEmail(account.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        Account createdAccount = accountService.save(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    // User Login (handled by Spring Security, no need for a separate endpoint if using JWT)
    // Consider adding JWT generation here if needed

    @GetMapping("/me")
    public ResponseEntity<Account> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Account> account = accountService.findByEmail(email);
            return account.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // Update Account
    @PutMapping("/me")
    public ResponseEntity<Account> updateAccount(@RequestBody Account updatedAccount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Account> account = accountService.findByEmail(email);
            if (account.isPresent()) {
                Account existingAccount = account.get();
                existingAccount.setFirstName(updatedAccount.getFirstName());
                existingAccount.setLastName(updatedAccount.getLastName());
                // Do not allow password updates without proper checks
                Account savedAccount = accountService.save(existingAccount);
                return ResponseEntity.ok(savedAccount);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<Account> account = accountService.findByEmail(email);
            if (account.isPresent()) {
                accountService.delete(account.get().getId());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAll();
        return ResponseEntity.ok(accounts);
    }
}
