package com.example.equipo.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.equipo.test.data.Answers;
import com.example.equipo.test.data.DatabaseHandler;
import com.example.equipo.test.data.Questions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityTest extends AppCompatActivity {
    @Bind({R.id.llA, R.id.llB, R.id.llC, R.id.llD, R.id.llE, R.id.llF, R.id.llG, R.id.llH}) List<LinearLayout> llAnswers;
    @Bind({R.id.rbA, R.id.rbB, R.id.rbC, R.id.rbD, R.id.rbE, R.id.rbF, R.id.rbG, R.id.rbH}) List<RadioButton> rbAnswers;
    @Bind({R.id.tvA, R.id.tvB, R.id.tvC, R.id.tvD, R.id.tvE, R.id.tvF, R.id.tvG, R.id.tvH}) List<TextView> tvAnswers;
    @Bind(R.id.tvNQuestion) TextView tvNQuestion;
    @Bind(R.id.tvTestName) TextView tvTestName;
    @Bind(R.id.tvQuestion) TextView tvQuestion;

    private final DatabaseHandler dbh = new DatabaseHandler(this);
    private ArrayList<Questions> questions = new ArrayList<>();
    private int currentQuestion = 0, score = 0, nQuestions = 0;
    private String testName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        testName = extras.getString("testName");
        questions = dbh.getQuestions(testName);
        tvTestName.setText(testName);
        nQuestions = questions.size();
        nextQuestion();
    }

    @OnClick({R.id.rbA, R.id.rbB, R.id.rbC, R.id.rbD, R.id.rbE, R.id.rbF, R.id.rbG, R.id.rbH}) void clickRadioButton(View v){
        checkAnswer(v);
        nextQuestion();
    }

    //COMPARA LA OPCION ELEGIDA CON LA RESPUESTA CORRECTA.
    private void checkAnswer(View v){
        int longV = v.toString().length();
        String eleccion = v.toString().charAt(longV - 2) + "";
        String respuesta = questions.get(currentQuestion -1).getCorrectOp();
        if(respuesta.equalsIgnoreCase(eleccion)) score++;
    }

    //SI EL CONTADOR (currentQuestion) NO SUPERA AL NUMERO DE PREGUNTAS TOTALES, VACIA LOS CAMPOS. SI LO SUPERA, PASAMOS A RESULTADOS
    private void nextQuestion(){
        currentQuestion++;
        if(!(currentQuestion > nQuestions)) {
            for(int i = 0; i < rbAnswers.size(); i++) rbAnswers.get(i).setChecked(false);

            tvNQuestion.setText(currentQuestion + "/" + questions.size());
            tvQuestion.setText(questions.get(currentQuestion - 1).getQuestion());

            for(int i = 0; i < llAnswers.size(); i++) llAnswers.get(i).setVisibility(View.GONE);

            ArrayList<Answers> answers = dbh.getAnswers(currentQuestion, testName);
            for(int i = 0; i < answers.size(); i++){
                llAnswers.get(i).setVisibility(View.VISIBLE);
                tvAnswers.get(i).setText(answers.get(i).getAnswer());
            }
        }
        else{
            Intent results = new Intent(getApplicationContext(), ActivityMarks.class);
            results.putExtra("score", score);
            results.putExtra("nQuestions", nQuestions);
            startActivity(results);
            finish();
        }
    }
}