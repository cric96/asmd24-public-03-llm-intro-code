package it.unibo.prompt.few;

import dev.langchain4j.model.chat.ChatModel;
import it.unibo.prompt.BasePromptBasedAgent;

import java.util.List;

public class FewShotAgent extends BasePromptBasedAgent {

    public FewShotAgent(
        ChatModel model,
        List<QuestionAnswer> questionAnswers
    ) {
        super(model, promptFromKnowledge(questionAnswers));
    }


    public static class QuestionAnswer {
        private String question;
        private String answer;
        public QuestionAnswer(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public static QuestionAnswer from(String question, String answer) {
            return new QuestionAnswer(question, answer);
        }
    }

    private static String promptFromKnowledge(List<QuestionAnswer> questions) {
        return "Giving this knowledge:" + questions.stream()
            .map(qa -> "Q:" + qa.question + "A:" + qa.answer)
            .reduce("", (acc, qa) -> acc + " \n " + qa) + "\n reply to the following question:";
    }

    @Override
    public String toString() {
        return "FewShotAgent";
    }
}
