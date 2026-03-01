package it.unibo.chat;

import it.unibo.utils.LlmConstants;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.List;

public class ChatBetweenAgents {
    public static void helloWorldAiWithTemperature() {
        final ChatModel model = OllamaChatModel.builder()
            .baseUrl(LlmConstants.OLLAMA_BASE_URL)
            .logRequests(true)
            .logResponses(true)
            .temperature(LlmConstants.DEFAULT_TEMPERATURE)
            .modelName(LlmConstants.CHAT_MODEL_SMOLLM)
            .numPredict(LlmConstants.MAX_PREDICT_TOKENS)
            .build();
        final var startMessage = "Hello, what is your name?";
        final ChatAgent leftAgent = ChatAgent.createChatAgent(
            model, List.of(UserMessage.from(startMessage))
        );
        final ChatAgent rightAgent = ChatAgent.createChatAgent(
            model,
            List.of(
                SystemMessage.from("Replies shortly, be rude!"),
                AiMessage.aiMessage(startMessage)
            )
        );
        for (int i = 0; i < 5; i++) {
            final String leftResponse = leftAgent.interact();
            System.out.println("Left: " + leftResponse);
            final String rightResponse = rightAgent.interact(leftResponse);
            System.out.println("Right: " + rightResponse);
            leftAgent.recordMessage(UserMessage.from(rightResponse));
        }
    }

    public static void main(String[] args) {
        helloWorldAiWithTemperature();
    }
}
