package com.jane.springaiintro.service;

import com.jane.springaiintro.model.*;

public interface OllamaAIService {
    String getAnswer(String message);
    Answer getAnswer(Question question);
    Answer getCapital(GetCapitalRequest request);
    GetCapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest request);
}
