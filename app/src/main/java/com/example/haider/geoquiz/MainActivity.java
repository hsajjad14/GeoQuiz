package com.example.haider.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private static final String KEY_INDEX = "index";
    private Button mCheatButton;
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;
    private Button mPrevButton;
    private TextView mCheated;


    private static final String IS_CHEAT = "cheat";

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_Canada, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),

    };
    private int mCurrentIndex = 0;

    private static final String TAG = "MainActivity";//chapter 3

    private boolean check = true;
    private int right = 0;
    private int cheated = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCheated = (TextView) findViewById(R.id.limted_cheats);
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        //add action listener to textview, so it goes to the next question when you click it
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        Log.d(TAG, "onCreate(Bundle) called");//chapter 3
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEAT, false);
        }


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);

            }
        });


        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });


        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);


            }
        });

        mPrevButton = (Button) findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;

                if (mCurrentIndex < 0){
                    mCurrentIndex = mCurrentIndex + mQuestionBank.length;
                }//test

                updateQuestion();

            }
        });
        updateQuestion();


    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);

            if (mIsCheater) {
                mQuestionBank[mCurrentIndex].setCheated(true);
                cheated = cheated-1;

                String ch = "";
                for (int i = 0; i<cheated;i++){
                    ch += "X";
                }
                mCheated.setText(ch);


            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(IS_CHEAT, mIsCheater);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int i = 0;
        int total = mQuestionBank.length;
        check = true;
        while (i<total && check){
            if(mQuestionBank[i].isAlreadyAnswered()){
                if(mQuestionBank[i].isAnswerTrue()){
                    check = true;
                }

            }else{
                check = false;
            }
            i++;
        }

        if(check) {
            double percent = (right*100)/total ;
            Log.i("MainActivity", "Amount i got right "+Integer.toString(right));
            Log.i("MainActivity", "total is "+Integer.toString(total));

            Toast.makeText(this, "You answered " + percent + "% of questions correct", Toast.LENGTH_SHORT).show();
        }else {
            int question = mQuestionBank[mCurrentIndex].getTextResId();
            mQuestionTextView.setText(question);
            mTrueButton.setEnabled(!mQuestionBank[mCurrentIndex].isAlreadyAnswered());
            mFalseButton.setEnabled(!mQuestionBank[mCurrentIndex].isAlreadyAnswered());
        }
        String str = String.valueOf(check);
        Log.i("MainActivity","check is: "+str);



    }

    private void checkAnswer(boolean userPressedTrue) {

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        check = true;
        int total = mQuestionBank.length;

        mTrueButton.setEnabled(mQuestionBank[mCurrentIndex].isAlreadyAnswered());
        mFalseButton.setEnabled(mQuestionBank[mCurrentIndex].isAlreadyAnswered());

        if (cheated == 0){
            mCheatButton.setEnabled(false);
        }else{
            mCheatButton.setEnabled(true);
        }

        int i = 0;
        int correct = 0;
        while (i<total && check){
            if(mQuestionBank[i].isAlreadyAnswered()){
                if(mQuestionBank[i].isAnswerTrue()){
                    check = true;
                }

            }else{
                check = false;
            }

            i++;
        }
        if(check){
            double percent = (right*100)/total ;
            Toast.makeText(this, "You answered "+percent+"% of questions correct",Toast.LENGTH_SHORT).show();

        }else {
            if (mQuestionBank[mCurrentIndex].isAlreadyAnswered()) {
                messageResId =  R.string.disabled_toast;
            }else {
                if (mIsCheater || mQuestionBank[mCurrentIndex].isCheated()) {
                    messageResId = R.string.judgement_toast;
                    if(userPressedTrue==answerIsTrue)
                        right=right+1;
                } else {
                    if (userPressedTrue == answerIsTrue) {
                        messageResId = R.string.correct_toast;
                        right = right + 1;

                    } else {
                        messageResId = R.string.incorrect_toast;
                    }
                }
            }

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            mQuestionBank[mCurrentIndex].setAlreadyAnswered(true);

        }
    }

}
