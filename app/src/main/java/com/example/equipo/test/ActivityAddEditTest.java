package com.example.equipo.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.example.equipo.test.data.Answers;
import com.example.equipo.test.data.DatabaseHandler;
import com.example.equipo.test.data.Questions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ActivityAddEditTest extends AppCompatActivity {
    @Bind({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.ll7, R.id.ll8}) List<LinearLayout> possibleAnswers;
    @Bind({R.id.etA, R.id.etB, R.id.etC, R.id.etD, R.id.etE, R.id.etF, R.id.etG, R.id.etH}) List<EditText> optionsTexts;
    @Bind({R.id.cbA, R.id.cbB, R.id.cbC, R.id.cbD, R.id.cbE, R.id.cbF, R.id.cbG, R.id.cbH}) List<CheckBox> checkboxes;
    @Bind(R.id.tvNQuestion) TextView tvNQuestion;
    @Bind(R.id.etNOptions) EditText etNOptions;
    @Bind(R.id.etQuestion) EditText etQuestion;

    private final DatabaseHandler dbh = new DatabaseHandler(this);
    private ArrayList<Questions> questions = new ArrayList<>();
    private ArrayList<Answers> answers = new ArrayList<>();

    private String correctOp = "", testName = "", testNameChanged = "";
    private int nQuestion = 0, nOptions = 2;
    private boolean modeAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_test);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent.hasExtra("testName")){
            modeAdd = false;
            Bundle extras = getIntent().getExtras();
            testName = extras.getString("testName");
            questions = dbh.getQuestions(testName);
            answers = dbh.getAnswers(testName);
            introduceTestNameDialog();
            extras.clear();
        }
        else {
            clearFields();
            introduceTestNameDialog();
        }
    }

    @OnClick(R.id.btnEnd) void clickEnd(){
        if(addUpdateQuestionsAndAnswers()){
            Toast.makeText(getApplicationContext(), "Test saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @OnClick(R.id.btnNext) void clickNext(){
        if(addUpdateQuestionsAndAnswers()){
            if(!modeAdd && nQuestion<questions.size()) fillFields();
            else{
                modeAdd = true;
                clearFields();
            }
            Toast.makeText(getApplicationContext(), "Question saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.cbA, R.id.cbB, R.id.cbC, R.id.cbD, R.id.cbE, R.id.cbF, R.id.cbG, R.id.cbH}) void clickCBA(CheckBox cb){
        checkOption(cb);
    }

    @OnTextChanged(R.id.etNOptions) void checkNOptions() throws NumberFormatException{
        try {
            if (!TextUtils.isEmpty(etNOptions.getText().toString()) && Integer.parseInt(etNOptions.getText().toString()) > possibleAnswers.size()) {
                etNOptions.setError("Max " + possibleAnswers.size() + " options");
                nOptions = possibleAnswers.size();
                for (int i = 0; i < nOptions; i++) possibleAnswers.get(i).setVisibility(View.VISIBLE);
            }
            else if (!TextUtils.isEmpty(etNOptions.getText().toString()) && Integer.parseInt(etNOptions.getText().toString()) >= 2) {
                nOptions = Integer.parseInt(etNOptions.getText().toString());
                for (int i = 0; i < nOptions; i++) possibleAnswers.get(i).setVisibility(View.VISIBLE);
            }
            else if (!TextUtils.isEmpty(etNOptions.getText().toString())) {
                etNOptions.setError("Min 2 options");
                nOptions = 2;
                for (int i = 2; i < possibleAnswers.size(); i++) possibleAnswers.get(i).setVisibility(View.GONE);
            }
            else {
                nOptions = 2;
                for (int i = 2; i < possibleAnswers.size(); i++) possibleAnswers.get(i).setVisibility(View.GONE);
            }
        } catch (Exception e){
            etNOptions.setError("Char not allowed");
        }
    }

    //SI LOS CAMPOS ESTAN RELLENOS CORRECTAMENTE, CREAMOS UNA ANSWER Y UNA QUESTION Y LOS METEMOS EN LA BASE DE DATOS
    private boolean addUpdateQuestionsAndAnswers(){
        Answers answer = new Answers();
        Questions question = new Questions();
        String questionText = etQuestion.getText().toString();

        if(!TextUtils.isEmpty(questionText) && !TextUtils.isEmpty(correctOp)){
            for(int i = 0; i<nOptions; i++) {
                if(possibleAnswers.get(i).getVisibility() == View.VISIBLE && !TextUtils.isEmpty(optionsTexts.get(i).getText().toString())) {

                    if(modeAdd) {
                        answer.setAnswer(optionsTexts.get(i).getText().toString());
                        answer.setQuestion(nQuestion);
                        answer.setTest(testName);
                        dbh.Add_Answer(answer);
                    }
                    else {
                        if (nOptions>answers.size()){
                            answer.setAnswer(optionsTexts.get(i).getText().toString());
                            answer.setQuestion(nQuestion);
                            answer.setTest(testName);
                            dbh.Add_Answer(answer);
                            answers = dbh.getAnswers(questions.get(nQuestion - 1).getNQuestion(), testName);
                        } else
                            for(int j = answers.size(); j>nOptions; j-- ) {
                                dbh.Delete_Answer(answers.get(j - 1).getId());
                            }
                        answer.setId(answers.get(i).getId());
                        answer.setAnswer(optionsTexts.get(i).getText().toString());
                        answer.setQuestion(nQuestion);
                        answer.setTest(testName);
                        dbh.Update_Answer(answer);
                    }
                } else{
                    Toast.makeText(this, getString(R.string.writePossibleOptions), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            if(modeAdd){
                question.setQuestion(questionText);
                question.setCorrectOp(correctOp);
                question.setTest(testName);
                question.setNQuestion(nQuestion);
                dbh.Add_Question(question);
            }
            else{
                question.setId(questions.get(nQuestion - 1).getId());
                question.setQuestion(questionText);
                question.setCorrectOp(correctOp);
                question.setTest(testName);
                question.setNQuestion(nQuestion);
                dbh.Update_Question(question);
            }
            return true;
        }
        else if(TextUtils.isEmpty(questionText) && !TextUtils.isEmpty(correctOp)){
            Toast.makeText(this, getString(R.string.writeQuestion), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!TextUtils.isEmpty(questionText) && correctOp.length()==0){
            Toast.makeText(this, getString(R.string.chooseCorrectAnswer), Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            Toast.makeText(this, getString(R.string.fillTheFields), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void clearFields(){
        nQuestion++;
        tvNQuestion.setText(" " + nQuestion + ":");

        etQuestion.setText("");
        etNOptions.setText("");

        for(int i = 0; i<possibleAnswers.size(); i++){
            checkboxes.get(i).setChecked(false);
            optionsTexts.get(i).setText("");
        }
    }

    private void fillFields(){
        nQuestion++;
        answers = dbh.getAnswers(nQuestion, testName);

        tvNQuestion.setText(" " + nQuestion + ":");

        etQuestion.setText(questions.get(nQuestion - 1).getQuestion());
        etNOptions.setText(answers.size() + "");
        checkNOptions();
        for(int i = 0; i < answers.size(); i++){
            possibleAnswers.get(i).setVisibility(View.VISIBLE);
            optionsTexts.get(i).setText(answers.get(i).getAnswer());
            String checkbox = checkboxes.get(i).getText().charAt(0) + "";
            if(checkbox.equalsIgnoreCase(questions.get(nQuestion - 1).getCorrectOp())){
                checkboxes.get(i).setChecked(true);
                checkOption(checkboxes.get(i));
            }
        }
    }

    //CUANDO CHEKAS UN BOX, DESCHECA LOS DEMAS (NO PERMITE MULTIPLES RESPUESTAS, TODAVIA...).
    private void checkOption(CheckBox cb){
        if(cb.isChecked()){
            correctOp = cb.getText().charAt(0) + "";
            for(int i = 0; i<checkboxes.size(); i ++){
                if(checkboxes.get(i).getId() != cb.getId()){
                    checkboxes.get(i).setChecked(false);
                }
            }
        } else correctOp = "";
    }

    private void introduceTestNameDialog(){
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(ActivityAddEditTest.this);
        if(modeAdd) {
            adb.setTitle(getString(R.string.createTest));
            adb.setMessage(getString(R.string.introduceName));
        } else{
            adb.setTitle(getString(R.string.editTest));
            adb.setMessage(getString(R.string.renameTest));
        }

        adb.setCancelable(false);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        adb.setView(input);
        input.setText(testName);

        if(modeAdd)
            adb.setNegativeButton(getString(R.string.cancel), new android.app.AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { finish(); }
            });
        else
            adb.setNegativeButton(getString(R.string.cancelRename), new android.app.AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    fillFields();
                }
            });

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(input.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.writeAName), Toast.LENGTH_SHORT).show();
                    introduceTestNameDialog();
                } else if (dbh.getTestsNames().contains(input.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nameAlreadyTaken), Toast.LENGTH_SHORT).show();
                    introduceTestNameDialog();
                } else if(modeAdd) testName = input.getText().toString();
                else{
                    testNameChanged = input.getText().toString();
                    updateTestName();
                }
            }
        });
        adb.show();
    }

    private void updateTestName(){
        for (Questions question : questions){
            question.setTest(testNameChanged);
            dbh.Update_Question(question);
        }

        for (Answers answer : answers){
            answer.setTest(testNameChanged);
            dbh.Update_Answer(answer);
        }
        testName = testNameChanged;
        questions = dbh.getQuestions(testName);

        fillFields();
    }
}