package com.example.SergioBlogs.services;

import com.example.SergioBlogs.models.Account;
import com.example.SergioBlogs.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public Account save(Account account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.saveAndFlush(account);
    }

    public Optional<Account> findByEmail(String email){
        return accountRepository.findByEmail(email);
    }

    public void delete(Long accountId){
        Optional<Account> account = accountRepository.findById(accountId);
        if(account.isPresent()){
            accountRepository.delete(account.get());
        } else{
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public List<Account> getAll(){
        return accountRepository.findAll();
    }
}
