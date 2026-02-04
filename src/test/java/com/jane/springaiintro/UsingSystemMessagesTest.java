package com.jane.springaiintro;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UsingSystemMessagesTest {

    @Autowired
    private ChatModel chatModel;

    @Test
    void cityGuideTest() {
        String systemPrompt = """
                You are a helpful assistant. Your role is a city tourism guide.
                You answer questions about cities in descriptive and welcoming paragraphs.
                You hope the user will visit and enjoy the city.
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage();

        PromptTemplate promptTemplate = new PromptTemplate("Tell me about New Orleans");
        Message userMessage = promptTemplate.createMessage();

        List<Message> messages = List.of(systemMessage, userMessage);

        Prompt prompt = new Prompt(messages);

        System.out.println(chatModel.call(prompt).getResult().getOutput().getText());
    }
}
