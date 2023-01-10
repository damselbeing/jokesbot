package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.entity.User;
import com.damselbeing.jokesbot.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;

@Service
@Slf4j
public class UserService {

    private final UserRepo repo;

    @Autowired
    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public void registerUser(Message msg) {

        if (!repo.existsById(msg.getChatId())) {
            User user = new User();
            user.setChatID(msg.getChatId());
            user.setFirstName(msg.getChat().getFirstName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            repo.save(user);
            log.info("A new User was registered: " + user);
        }
    }

    public void deleteUser(long chatId) {

        if(repo.existsById(chatId)) {
            repo.deleteById(chatId);
            log.info("User with chatId " + chatId + " was deleted.");
        }
    }
}
