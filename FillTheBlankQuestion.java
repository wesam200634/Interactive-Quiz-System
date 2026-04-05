
package quizproject;


public class FillTheBlankQuestion extends Question {
    @Override
    public boolean checkAnswer(Answer a){
         return correctAnswer.toLowerCase().equals(a.getResponse());
    }
}
