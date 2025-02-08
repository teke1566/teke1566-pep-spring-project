package com.example.service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
   @Autowired
   private AccountRepository accountRepository;
   public Account registerAccount(Account account) {
       if (account.getUsername() == null || account.getUsername().isBlank() ||
           account.getPassword() == null || account.getPassword().length() < 4) {
           return null;
       }
       if (accountRepository.findByUsername(account.getUsername()) != null) {
           throw new IllegalArgumentException("Username already exists");
       }
       return accountRepository.save(account);
   }
   public Account login(String username, String password) {
       Account account = accountRepository.findByUsername(username);
       if (account != null && account.getPassword().equals(password)) {
           return account;
       }
       return null;
   }
   public boolean accountExists(Integer accountId) {
    return accountRepository.existsById(accountId);
}



}



