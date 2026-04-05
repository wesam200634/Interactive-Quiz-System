
package quizproject;

import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private List<String> choices;
    public void displayChoices(){
        System.out.println(choices);
          
        }
          @Override
    public boolean checkAnswer(Answer a){
     
         return correctAnswer.equals(a.getResponse());
    }
   
    }

