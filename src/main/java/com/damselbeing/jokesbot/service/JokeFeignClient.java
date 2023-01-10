package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.entity.Joke;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jokeFeignClient", url = "https://v2.jokeapi.dev/joke/Programming?type=twopart")
public interface JokeFeignClient {

    @GetMapping
    Joke getJoke();

}
