package com.damselbeing.jokesbot.service;

import com.damselbeing.jokesbot.model.Joke;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jokeFeignClient", url = "${joke.api}")
public interface JokeFeignClient {

    @GetMapping
    Joke getJoke();

}
