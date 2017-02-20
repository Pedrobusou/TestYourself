package com.example.equipo.test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.equipo.test.data.DatabaseHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityMain extends AppCompatActivity {
    @Bind(R.id.testsList) Spinner testsList;
    private final DatabaseHandler dbh = new DatabaseHandler(this);
    private String testName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fillSpinner();
    }

    @OnClick(R.id.btnStart) void clickStart(){
        if(testsList.getSelectedItem()!=null){
            testName = testsList.getSelectedItem().toString();
            Intent test = new Intent(getApplicationContext(), ActivityTest.class);
            test.putExtra("testName", testName);
            startActivity(test);
        } else Toast.makeText(getApplicationContext(), getString(R.string.createTest), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnEdit) void clickEdit(){
        if(testsList.getSelectedItem()!=null){
            testName = testsList.getSelectedItem().toString();
            Intent edit = new Intent(getApplicationContext(), ActivityAddEditTest.class);
            edit.putExtra("testName", testName);
            startActivity(edit);
        } else Toast.makeText(getApplicationContext(), getString(R.string.createTest), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnDelete) void clickDelete(){
        if(testsList.getSelectedItem()!=null){
            testName = testsList.getSelectedItem().toString();
            confirmDelete();
        } else Toast.makeText(getApplicationContext(), getString(R.string.createTest), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnCreate) void clickCreate(){
        Intent createTest = new Intent(getApplicationContext(), ActivityAddEditTest.class);
        startActivity(createTest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillSpinner();
    }

    private void fillSpinner(){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dbh.getTestsNames());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testsList.setAdapter(spinnerAdapter);
    }

    private void confirmDelete(){
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(ActivityMain.this);
            adb.setTitle(getString(R.string.deleteTest));
            adb.setMessage(getString(R.string.deleteTestConfirmation) + testName + "\"?");

            adb.setNegativeButton(getString(R.string.cancel), new android.app.AlertDialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {}
            });

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbh.Delete_Test(testName);
                fillSpinner();
            }
        });
        adb.show();
    }
}