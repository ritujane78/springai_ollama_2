package com.jane.springaiintro;

import com.jane.springaiintro.service.OllamaAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OllamaAIServiceImplTest {

    @Autowired
    private OllamaAIService service;

    @Test
    void getAnswer() {
    String answer = service.getAnswer("what is life?");

    System.out.println(answer);
    }
}