package com.damselbeing.jokesbot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${telegram.botName}")
    private String botName;

    @Value("${telegram.botToken}")
    private String botToken;

}
