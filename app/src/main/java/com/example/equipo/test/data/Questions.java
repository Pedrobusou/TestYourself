package com.example.equipo.test.data;

public class Questions {
    private String question, correctOp, test;
    private int id, nQuestion;

    public Questions(){}

    public Questions(int id, String question, String correctOp, String test, int nQuestion){
        this.id = id;
        this.question = question;
        this.correctOp = correctOp;
        this.test = test;
        this.nQuestion = nQuestion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectOp() {
        return correctOp;
    }

    public void setCorrectOp(String correctOp) {this.correctOp = correctOp;}

    public String getTest() {return test;}

    public void setTest(String test) {this.test = test;}

    public int getNQuestion() {
        return nQuestion;
    }

    public void setNQuestion(int nQuestion) {
        this.nQuestion = nQuestion;
    }
}