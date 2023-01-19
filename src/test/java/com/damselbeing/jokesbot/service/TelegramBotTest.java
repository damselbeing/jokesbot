package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.config.BotConfig;
import com.damselbeing.jokesbot.model.User;
import com.damselbeing.jokesbot.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@ExtendWith(MockitoExtension.class)
class TelegramBotTest {

    @Mock
    UserRepo repo;

    @Mock
    JokeFeignClient jokeFeignClient;

    @Mock
    Message msg;

    @Mock
    Chat chat;

    @Mock
    Update update;

    private TelegramBot telegramBot;
    private BotConfig config;
    private String botName;
    private String botToken;

    @BeforeEach
    void setUp() {
        botName = "CodeJokesBot";
        botToken = "333";
        config = new BotConfig();
        config.setBotName(botName);
        config.setBotToken(botToken);
        telegramBot = spy(new TelegramBot(repo, config, jokeFeignClient));
    }

    @Test
    void shouldStartOnUpdateReceived() throws Exception {
        String text = "/start";
        Long chatId = 111L;
        String firstName = "Anna";
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(msg);
        when(msg.hasText()).thenReturn(true);
        when(msg.getText()).thenReturn(text);
        when(msg.getChatId()).thenReturn(chatId);
        when(msg.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(firstName);

        telegramBot.onUpdateReceived(update);

        verifyPrivate(telegramBot).invoke("startCommandReceived", chatId, firstName);
        verifyPrivate(telegramBot).invoke("registerUser", msg);

    }

    @Test
    void shouldDeleteOnUpdateReceived() throws Exception {
        String text = "/deletedata";
        Long chatId = 111L;
        String firstName = "Anna";
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(msg);
        when(msg.hasText()).thenReturn(true);
        when(msg.getText()).thenReturn(text);
        when(msg.getChatId()).thenReturn(chatId);
        when(msg.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(firstName);

        telegramBot.onUpdateReceived(update);

        verifyPrivate(telegramBot).invoke("deleteCommandReceived", chatId);

    }

    @Test
    void shouldJokeOnUpdateReceived() throws Exception {
        String text = "/wannajoke";
        Long chatId = 111L;
        String firstName = "Anna";
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(msg);
        when(msg.hasText()).thenReturn(true);
        when(msg.getText()).thenReturn(text);
        when(msg.getChatId()).thenReturn(chatId);
        when(msg.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(firstName);

        telegramBot.onUpdateReceived(update);

        verifyPrivate(telegramBot).invoke("wannaCommandReceived", chatId);
        verifyPrivate(telegramBot).invoke("registerUser", msg);

    }

    @Test
    void shouldDeleteUser() {
        Long chatIdExists = 111L;
        when(repo.existsById(chatIdExists)).thenReturn(true);

        telegramBot.deleteCommandReceived(chatIdExists);

        verify(repo).deleteById(chatIdExists);
    }

    @Test
    void shouldNotDeleteUser() {
        Long chatIdDoesNotExist = 222L;
        when(repo.existsById(chatIdDoesNotExist)).thenReturn(false);

        telegramBot.deleteCommandReceived(chatIdDoesNotExist);

        verify(repo, never()).deleteById(chatIdDoesNotExist);
    }

    @Test
    void shouldRegisterUser() {
        Long chatId = 111L;
        String firstName = "Anna";
        LocalDateTime registeredAt = LocalDateTime.now();
        User user = new User(chatId, firstName, registeredAt);
        when(msg.getChatId()).thenReturn(chatId);
        when(msg.getChat()).thenReturn(chat);
        when(chat.getFirstName()).thenReturn(firstName);

        telegramBot.registerUser(msg);

        verify(repo).save(user);
    }


}