package com.feedhanjum.back_end.feedback.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatGPTClient {
    private final OpenAiChatModel openAiChatModel;

    public Flux<String> callAiRefining(String message) {
        return openAiChatModel.stream(new Prompt(message))
                .map(chatResponse -> Optional.ofNullable(chatResponse.getResult())
                        .map(Generation::getOutput)
                        .map(AssistantMessage::getText))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}
