package com.example.controller;


import org.springframework.web.bind.annotation.RequestMapping;


import com.example.service.AccountService;
import com.example.service.MessageService;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AccountNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
* TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
* found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
* where applicable as well as the @ResponseBody and @PathVariable annotations. You should
* refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
*/




import java.util.List;


@RestController
@RequestMapping
public class SocialMediaController {
   @Autowired
   private AccountService accountService;


   @Autowired
   private MessageService messageService;


   @PostMapping("/register")
   public ResponseEntity<?> register(@RequestBody Account account) {
       try {
           Account newAccount = accountService.registerAccount(account);
           if (newAccount == null) {
               return ResponseEntity.badRequest().build();
           }
           return ResponseEntity.ok(newAccount);
       } catch (IllegalArgumentException e) {
           return ResponseEntity.status(409).body(e.getMessage());
       }
   }


   @PostMapping("/login")
   public ResponseEntity<?> login(@RequestBody Account account) {
       Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
       if (loggedInAccount == null) {
           return ResponseEntity.status(401).build();
       }
       return ResponseEntity.ok(loggedInAccount);
   }




   @PostMapping("/messages")
public ResponseEntity<?> createMessage(@RequestBody Message message) {
    System.out.println("DEBUG: Received createMessage request: " + message);

    if (message.getPostedBy() == null) {
        System.out.println("DEBUG: Missing postedBy field!");
        return ResponseEntity.status(400).body("Missing postedBy field.");
    }

    if (!accountService.accountExists(message.getPostedBy())) {
        System.out.println("DEBUG: Account with ID " + message.getPostedBy() + " does not exist!");
        return ResponseEntity.status(400).body("Invalid or non-existent account ID: " + message.getPostedBy());
    }

    if (message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
        System.out.println("DEBUG: Invalid message text!");
        return ResponseEntity.badRequest().body("Invalid message content.");
    }

    System.out.println("DEBUG: Calling messageService.createMessage()...");
    Message newMessage = messageService.createMessage(message);

    if (newMessage == null) {
        System.out.println("DEBUG: Message creation failed!");
        return ResponseEntity.status(500).body("Message could not be created due to an internal error.");
    }

    System.out.println("DEBUG: Message created successfully: " + newMessage);
    return ResponseEntity.ok(newMessage);
}
   @GetMapping("/messages")
   public ResponseEntity<List<Message>> getAllMessages() {
       return ResponseEntity.ok(messageService.getAllMessages());
   }
   @GetMapping("/messages/{messageId}")
   public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
       return ResponseEntity.ok(messageService.getMessageById(messageId));
   }

   @DeleteMapping("/messages/{messageId}")
   public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
       if (messageService.deleteMessage(messageId)) {
           return ResponseEntity.ok(1);
       }
       return ResponseEntity.ok().build();
   }

   @PatchMapping("/messages/{messageId}")
   public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
       if (messageService.updateMessage(messageId, message.getMessageText())) {
           return ResponseEntity.ok(1);
       }
       return ResponseEntity.badRequest().build();
   }

   @GetMapping("/accounts/{accountId}/messages")
   public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
       return ResponseEntity.ok(messageService.getMessagesByUserId(accountId));
   }
}
