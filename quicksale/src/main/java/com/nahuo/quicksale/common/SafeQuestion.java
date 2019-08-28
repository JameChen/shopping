package com.nahuo.quicksale.common;

public class SafeQuestion {

    private int questionId;
    private String question;
    private String answer;
    
    public SafeQuestion(){
        
    }
    public SafeQuestion(int id, String question){
        this.questionId = id;
        this.question = question;
    }
    public int getQuestionId() {
        return questionId;
    }
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    @Override
    public String toString() {
        return question;
    }
    
    
}
