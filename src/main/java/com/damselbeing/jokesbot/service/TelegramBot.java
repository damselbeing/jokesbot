package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    BotConfig config;

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
            String msgText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String usersFirstName = update.getMessage().getChat().getFirstName();

            switch (msgText) {
                case "/start" -> startCommandReceived(chatId, usersFirstName);
                default -> sendMsg(chatId, "Sorry, this command was not recognised.");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!";
        sendMsg(chatId, answer);
    }

    private void sendMsg(long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
