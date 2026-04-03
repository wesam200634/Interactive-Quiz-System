
package quizproject;


public class TrueFalseQuestion extends Question {
    @Override//this helps us know that this method is inherited from the father but this child change the way it works on its own behaviour
    public boolean checkAnswer(Answer a){
        return true;
    }
}
