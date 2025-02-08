package com.example.service;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class MessageService {
   @Autowired
   private MessageRepository messageRepository;
   @Autowired
   private AccountService accountService;
   public Message createMessage(Message message) {
    System.out.println("DEBUG: Entering createMessage()");
    System.out.println("DEBUG: Message Received: " + message);

    if (message.getMessageText() == null || message.getMessageText().isBlank() ||
        message.getMessageText().length() > 255) {
        System.out.println("DEBUG: Invalid message content!");
        return null;
    }

    System.out.println("DEBUG: Checking if account exists for ID: " + message.getPostedBy());

    if (!accountService.accountExists(message.getPostedBy())) {
        System.out.println("DEBUG: Account with ID " + message.getPostedBy() + " does not exist!");
        return null;
    }

    System.out.println("DEBUG: Saving message to the database...");
    Message savedMessage = messageRepository.save(message);
    
    System.out.println("DEBUG: Message successfully saved! " + savedMessage);
    return savedMessage;
}

   public List<Message> getAllMessages() {
       return messageRepository.findAll();
   }

   public Message getMessageById(Integer messageId) {
       return messageRepository.findById(messageId).orElse(null);
   }

   public List<Message> getMessagesByUserId(Integer userId) {
       return messageRepository.findByPostedBy(userId);
   }
   public boolean deleteMessage(Integer messageId) {
       if (messageRepository.existsById(messageId)) {
           messageRepository.deleteById(messageId);
           return true;
       }
       return false;
   }
   public boolean updateMessage(Integer messageId, String newText) {
       if (newText == null || newText.isBlank() || newText.length() > 255) {
           return false;
       }
       Message message = messageRepository.findById(messageId).orElse(null);
       if (message != null) {
           message.setMessageText(newText);
           messageRepository.save(message);
           return true;
       }
       return false;
   }
}
