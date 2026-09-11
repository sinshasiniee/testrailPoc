package com.example.testrailPoc.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LlmRunbookClient {

    private final ChatClient chatClient;

    public LlmRunbookClient(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder.build();
    }

    public String generate(String prompt) throws IOException, InterruptedException {


        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        System.out.println("AI response: {}"+ aiResponse);
        return aiResponse;

    }
}
