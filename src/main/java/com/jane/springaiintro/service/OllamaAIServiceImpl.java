package com.jane.springaiintro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jane.springaiintro.model.Answer;
import com.jane.springaiintro.model.GetCapitalRequest;
import com.jane.springaiintro.model.GetCapitalResponse;
import com.jane.springaiintro.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;

@Service
public class OllamaAIServiceImpl implements OllamaAIService {


    private final Logger logger = LoggerFactory.getLogger(OllamaAIServiceImpl.class);

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;
    @Value("classpath:templates/get-capital-prompt.st")
    private Resource promptTemplateResource;

    @Value("classpath:templates/get-capital-prompt-with-info.st")
    private Resource promptTemplateInfoResource;


    @Autowired
    public OllamaAIServiceImpl(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }
    @Override
    public String getAnswer(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate((question.question()));
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);


        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapital(GetCapitalRequest request) {
//        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
//        String format = converter.getFormat();
//        System.out.println("format: \n" + format);

        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", request.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);
        String raw = response.getResult().getOutput().getText();
        logger.info("LLM raw response: {}", raw);

        JsonNode jsonNode = objectMapper.readTree(raw);
        return new Answer(jsonNode.get("answer").asText());
//        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest request) {
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateInfoResource);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", request.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }
}
