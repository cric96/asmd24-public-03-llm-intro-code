package it.unibo.prompt;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;

/**
 * An abstract base class for all prompting based agents.
 * It provides the model to be used for chatting and a base prompt for the agent.
 */
public abstract class BasePromptBasedAgent implements PromptBasedAgent {
    private final String promptBase;
    private final ChatModel model;
    public BasePromptBasedAgent(ChatModel model, String promptBase) {
        this.promptBase = promptBase;
        this.model = model;
    }

    /**
     * Get the model used by the agent.
     * @return The model used by the agent.
     */
    protected ChatModel getModel() {
        return model;
    }

    public String getPromptBase() {
        return promptBase;
    }

    public String ask(String userMessage) {
        System.out.println(this.prepareMessage(userMessage));
        return getModel().chat(
            UserMessage.from(this.prepareMessage(userMessage))
        ).aiMessage().text();
    }

    /**
     * Prepare the message to be sent to the model.
     * @param userMessage The user's message.
     * @return The message combined with the base prompt.
     */
    protected String prepareMessage(String userMessage) {
        return getPromptBase() + userMessage;
    }
}
