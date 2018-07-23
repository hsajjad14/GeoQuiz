package com.example.haider.geoquiz;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mCheated;
    private boolean mAlreadyAnswered = false;


    public boolean isAlreadyAnswered() {
        return mAlreadyAnswered;
    }

    public void setAlreadyAnswered(boolean alreadyAnswered) {
        mAlreadyAnswered = alreadyAnswered;
    }



    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }



    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }




}
