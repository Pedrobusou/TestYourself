package com.example.equipo.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActivityMarks extends AppCompatActivity {
    @Bind(R.id.tvRate) TextView tvRate;
    @Bind(R.id.tvScore) TextView tvScore;
    @Bind(R.id.tvMark) TextView tvMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        int score = extras.getInt("score");
        int nQuestions = extras.getInt("nQuestions");

        tvScore.setText(score + "/" + nQuestions);
        tvRate.setText((score * 100) / nQuestions + "%");
        tvMark.setText((score * 10) / nQuestions + "/" + "10");
    }
}