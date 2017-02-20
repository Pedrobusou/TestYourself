package com.example.equipo.test.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tests";

    private final String TABLE_QUESTIONS = "questions";
    private final String QUEST_ID = "id";
    private final String QUEST_QUESTION = "question";
    private final String QUEST_CORRECT_OP = "correctOp";
    private final String QUEST_TEST = "testName";
    private final String QUEST_nQUESTION = "nQuestion";

    private final String TABLE_ANSWERS = "answers";
    private final String ANS_ID = "id";
    private final String ANS_ANSWER = "answer";
    private final String ANS_nQUESTION = "nQuestion";
    private final String ANS_TEST = "testName";

    private final ArrayList<Questions> questions_list = new ArrayList<>();
    private final ArrayList<Answers> answers_list = new ArrayList<>();
    private final ArrayList<String> tests_list = new ArrayList<>();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "(" + QUEST_ID + " INTEGER PRIMARY KEY," + QUEST_QUESTION + " TEXT,"
                + QUEST_CORRECT_OP + " TEXT," + QUEST_TEST + " TEXT," + QUEST_nQUESTION + " INTEGER)";
        db.execSQL(CREATE_QUESTIONS_TABLE);

        String CREATE_ANSWERS_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "(" + ANS_ID + " INTEGER PRIMARY KEY," + ANS_ANSWER + " TEXT,"
                + ANS_nQUESTION + " INTEGER," + ANS_TEST + " TEXT" +")";
        db.execSQL(CREATE_ANSWERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);

        // Create tables again
        onCreate(db);
    }

    // ADD QUESTION/ANSWER
    public void Add_Question(Questions question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QUEST_QUESTION, question.getQuestion()); // Question text
        values.put(QUEST_CORRECT_OP, question.getCorrectOp()); // Correct option
        values.put(QUEST_TEST, question.getTest()); // Name of the testName the question belongs to
        values.put(QUEST_nQUESTION, question.getNQuestion());

        db.insert(TABLE_QUESTIONS, null, values);
        db.close(); // Closing database connection
    }

    public void Add_Answer(Answers answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ANS_ANSWER, answer.getAnswer()); // Answer text
        values.put(ANS_nQUESTION, answer.getQuestion()); // Question that corresponds
        values.put(ANS_TEST, answer.getTest());

        db.insert(TABLE_ANSWERS, null, values);
        db.close(); // Closing database connection
    }

    //GET ALL QUESTIONS/ANSWERS/TESTSNAMES
    public ArrayList<Questions> getQuestions(String test) {
        try {
            questions_list.clear();
            String selectQuery = "SELECT * FROM " + TABLE_QUESTIONS + " WHERE " + QUEST_TEST + " LIKE " + "'" + test + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Questions question = new Questions();
                    question.setId(Integer.parseInt(cursor.getString(0)));
                    question.setQuestion(cursor.getString(1));
                    question.setCorrectOp(cursor.getString(2));
                    question.setTest(cursor.getString(3));
                    question.setNQuestion(Integer.parseInt(cursor.getString(4)));

                    questions_list.add(question);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return questions_list;
        } catch (Exception e) {
            Log.e("all_questions", "" + e);
        }
        return questions_list;
    }

    public ArrayList<Answers> getAnswers(String testName) {
        try {
            answers_list.clear();
            String selectQuery = "SELECT * FROM " + TABLE_ANSWERS + " WHERE " + ANS_TEST + " LIKE '" + testName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Answers answer = new Answers();
                    answer.setId(Integer.parseInt(cursor.getString(0)));
                    answer.setAnswer(cursor.getString(1));
                    answer.setQuestion(Integer.parseInt(cursor.getString(2)));
                    answer.setTest(cursor.getString(3));

                    answers_list.add(answer);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return answers_list;
        } catch (Exception e) {
            Log.e("all_answers", "" + e);
        }
        return answers_list;
    }

    public ArrayList<Answers> getAnswers(int nQuestion, String testName) {
        try {
            answers_list.clear();
            String selectQuery = "SELECT * FROM " + TABLE_ANSWERS + " WHERE " + ANS_nQUESTION + " = " + nQuestion + " AND " + ANS_TEST + " LIKE '" + testName + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Answers answer = new Answers();
                    answer.setId(Integer.parseInt(cursor.getString(0)));
                    answer.setAnswer(cursor.getString(1));
                    answer.setQuestion(Integer.parseInt(cursor.getString(2)));
                    answer.setTest(cursor.getString(3));

                    answers_list.add(answer);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return answers_list;
        } catch (Exception e) {
            Log.e("all_answers", "" + e);
        }
        return answers_list;
    }

    public ArrayList<String> getTestsNames() {
        try {
            tests_list.clear();
            String selectQuery = "SELECT  DISTINCT " + QUEST_TEST + " FROM " + TABLE_QUESTIONS;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    String testName = cursor.getString(0);
                    tests_list.add(testName);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tests_list;
        } catch (Exception e) {
            Log.e("all_questions", "" + e);
        }
        return tests_list;
    }

    ///////////////////////////////////////////////////////////////////////////////EDICION DE DATOS
    // GET SINGLE QUESTION/ANSWER   PUEDE SER UTIL PA LA EDICION, SI NO BORRAMOS
    public Questions getQuestion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] {QUEST_ID, QUEST_QUESTION, QUEST_CORRECT_OP, QUEST_TEST}, QUEST_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null) cursor.moveToFirst();

        Questions question = new Questions(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));

        cursor.close();
        db.close();

        return question;
    }

    // UPDATE SINGLE QUESTION/ANSWER
    public void Update_Question(Questions question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUEST_QUESTION,   question.getQuestion());
        values.put(QUEST_CORRECT_OP, question.getCorrectOp());
        values.put(QUEST_TEST,       question.getTest());
        values.put(QUEST_nQUESTION,  question.getNQuestion());

        db.update(TABLE_QUESTIONS, values, QUEST_ID + " = ?", new String[]{String.valueOf(question.getId())});
        db.close();
    }

    public void Update_Answer(Answers answer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANS_ANSWER,      answer.getAnswer());
        values.put(ANS_nQUESTION,   answer.getQuestion());
        values.put(ANS_TEST,        answer.getTest());

        db.update(TABLE_ANSWERS, values, ANS_ID + " = ?", new String[] { String.valueOf(answer.getId()) });
        db.close();
    }

    // DELETE ALL QUESTIONS/ANSWERS FROM A TEST
    public void Delete_Test(String testName) {
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        query = "DELETE FROM " + TABLE_ANSWERS + " WHERE " + ANS_TEST + " LIKE '" + testName + "'";
        db.execSQL(query);
        query = "DELETE FROM " + TABLE_QUESTIONS + " WHERE " + QUEST_TEST + " LIKE '" + testName + "'";
        db.execSQL(query);
        db.close();
    }

    public void Delete_Answer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_ANSWERS + " WHERE " + ANS_ID + " = " + id;
        db.execSQL(query);
        db.close();
    }
}