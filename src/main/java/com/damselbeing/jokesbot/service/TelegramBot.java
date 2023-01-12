package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.config.BotConfig;
import com.damselbeing.jokesbot.model.Joke;
import com.damselbeing.jokesbot.model.User;
import com.damselbeing.jokesbot.repository.UserRepo;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final UserRepo repo;
    private final BotConfig config;
    private final JokeFeignClient jokeFeignClient;

    @Autowired
    public TelegramBot(UserRepo repo,
                       BotConfig config,
                       JokeFeignClient jokeFeignClient) {
        this.repo = repo;
        this.config = config;
        this.jokeFeignClient = jokeFeignClient;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/wannajoke", "get a random joke"));
        listOfCommands.add(new BotCommand("/deletedata", "delete your data stored"));
        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occurred by setting bots command list: " + e.getMessage());
        }
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
                    registerUser(msg);
                }
                case "/wannajoke" -> {
                    wannaCommandReceived(chatId);
                    registerUser(msg);
                }
                case "/deletedata" -> deleteCommandReceived(chatId);
                default -> sendMsg(chatId, "Sorry, this command was not recognised.");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode(
                "Hi" + ":wave: " + name + ", nice to meet you!" + "\n"
                        + "Guess, you /wannajoke?");
        sendMsg(chatId, answer);
        log.info("Replied 'Hello' to User with chatId " + chatId);
    }

    @Transactional
    public void deleteCommandReceived(long chatId) {
        if(repo.existsById(chatId)) {
            repo.deleteById(chatId);
            log.info("User with chatId " + chatId + " was deleted.");
        }
        String answer = EmojiParser.parseToUnicode(
                "All your data has been successfully deleted" + ":ok_hand:");
        sendMsg(chatId, answer);
    }

    @Transactional
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

    private void wannaCommandReceived(long chatId) {
        Joke joke = jokeFeignClient.getJoke();
        String answer = EmojiParser.parseToUnicode(
                "Alice: " + joke.getSetup() + "\n"
                + "Bob: " + joke.getDelivery() + " :joy:");
        sendMsg(chatId, answer);
        log.info("Replied 'Joke' " + joke + " to User with chatId " + chatId);
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
