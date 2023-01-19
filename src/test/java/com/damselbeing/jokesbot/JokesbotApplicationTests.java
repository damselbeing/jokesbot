package com.damselbeing.jokesbot;

import jakarta.ws.rs.core.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Application.class)
class JokesbotApplicationTests {

	@Test
	void contextLoads() {
	}

}
