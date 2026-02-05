package com.jane.springaiintro.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jane.springaiintro.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
@RequiredArgsConstructor
@Service
public class OllamaAIServiceImpl implements OllamaAIService {


    private final Logger logger = LoggerFactory.getLogger(OllamaAIServiceImpl.class);

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;
    private final SimpleVectorStore vectorStore;

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource promptTemplateResource;

    @Value("classpath:templates/get-capital-prompt-with-info.st")
    private Resource promptTemplateInfoResource;

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Override
    public String getAnswer(String message) {
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }

    @Override
    public Answer getAnswer(Question question) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .query(question.question()).topK(5).build());
        List<String> currentList = documents.stream().map(Document::getText).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Prompt prompt = promptTemplate.create(Map.of("input", question.question(), "documents", String.join("\n", currentList)));
        currentList.forEach(System.out::println);
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

//    @Override
//    public Answer getCapitalWithInfo(GetCapitalRequest request) {
//        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateInfoResource);
//        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", request.stateOrCountry()));
//        ChatResponse response = chatModel.call(prompt);
//
//        return new Answer(response.getResult().getOutput().getText());
//    }
    @Override
    public GetCapitalWithInfoResponse getCapitalWithInfo(GetCapitalRequest request) {
//        BeanOutputConverter<GetCapitalWithInfoResponse> converter = new BeanOutputConverter<>(GetCapitalWithInfoResponse.class);
//        String format = converter.getFormat();
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateInfoResource);
        Prompt prompt = promptTemplate.create(
                Map.of("stateOrCountry", request.stateOrCountry())
        );

        ChatResponse response = chatModel.call(prompt);
        String raw = response.getResult().getOutput().getText();
        logger.info("LLM raw response: {}", raw);

        JsonNode root = objectMapper.readTree(raw);

//        return converter.convert(Objects.requireNonNull(response.getResult().getOutput().getText()));
        return new GetCapitalWithInfoResponse(
                root.path("city").asText(null),
                root.path("population").isMissingNode()
                        ? null
                        : root.path("population").asInt(), // handles "181011" safely
                root.path("region").asText(null),
                root.path("language").asText(null),
                root.path("currency").asText(null)
        );
    }

}
