package com.jane.springaiintro.service;

import com.jane.springaiintro.model.Answer;
import com.jane.springaiintro.model.GetCapitalRequest;
import com.jane.springaiintro.model.GetCapitalResponse;
import com.jane.springaiintro.model.Question;

public interface OllamaAIService {
    String getAnswer(String message);
    Answer getAnswer(Question question);
    Answer getCapital(GetCapitalRequest request);
    Answer getCapitalWithInfo(GetCapitalRequest request);
}
