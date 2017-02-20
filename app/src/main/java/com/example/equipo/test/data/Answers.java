package com.example.equipo.test.data;

public class Answers {
    private String answer, testName;
    private int id, question;

    public Answers(){}

    /*public Answers(int id, String answer, int question, String testName){
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.testName = testName;
    }*/

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest() {
        return testName;
    }

    public void setTest(String testName) {
        this.testName = testName;
    }
}