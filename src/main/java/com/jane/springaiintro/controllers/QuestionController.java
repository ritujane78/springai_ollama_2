package com.jane.springaiintro.controllers;

import com.jane.springaiintro.model.*;
import com.jane.springaiintro.service.OllamaAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {
    private OllamaAIService service;
    public QuestionController(OllamaAIService service){
        this.service = service;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question){
        return service.getAnswer(question);
    }

    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return service.getCapital(getCapitalRequest);
    }

    @PostMapping("/capitalWithInfo")
    public GetCapitalWithInfoResponse getCapitalWithInfo(@RequestBody GetCapitalRequest getCapitalRequest) {
        return service.getCapitalWithInfo(getCapitalRequest);
    }
}
