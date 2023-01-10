package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final UserService userService;

    @Autowired
    public TelegramBot(BotConfig config, UserService userService) {
        this.config = config;
        this.userService = userService;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            Message msg = update.getMessage();
            String msgText = msg.getText();
            long chatId = update.getMessage().getChatId();
            String usersFirstName = update.getMessage().getChat().getFirstName();

            switch (msgText) {
                case "/start" -> {
                    startCommandReceived(chatId, usersFirstName);
                    userService.registerUser(msg);
                }
                case "/deleteMe" -> {
                    deleteCommandReceived(chatId);
                }
                default -> sendMsg(chatId, "Sorry, this command was not recognised.");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMsg(chatId, answer);
        log.info("Replied 'Start' to User with chatId " + chatId);
    }

    private void deleteCommandReceived(long chatId) {
        userService.deleteUser(chatId);
        String answer = "All your data has been successfully deleted!";
        sendMsg(chatId, answer);
    }

    private void sendMsg(long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.error("Error occurred by sending message: " + e.getMessage());
        }
    }
}
