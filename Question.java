package quizproject;

public abstract class Question {

    private String questionText;
    private String correctAnswer;
    private double points;
    private double timeLimit;

    //since every child class will check the answer in different way we should abstract the father.
    //abstracing the method means every child class will use this class in its unique way
    public abstract boolean checkAnswer(Answer a);

    //here the display will be the same for the children,no need for abstracting.
    public void displayQuestion() {
        System.out.println(questionText);
    }
}