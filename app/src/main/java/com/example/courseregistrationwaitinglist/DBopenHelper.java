package com.example.courseregistrationwaitinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBopenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "student.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_STUDENT = "student";
    public static final String STUDENT_ID = "_id";
    public static final String STUDENT_FN = "firstname";
    public static final String STUDENT_LN = "lastname";
    public static final String STUDENT_PRIORITY = "priority";
    public static final String STUDENT_CREATED = "studentCreated";

    public static final String[] ALL_COLUMNS =
            {STUDENT_ID,STUDENT_FN,STUDENT_LN,STUDENT_PRIORITY,STUDENT_CREATED};


    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_STUDENT + " ( " +
                    STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    STUDENT_FN + " TEXT, " +
                    STUDENT_LN + " TEXT, " +
                    STUDENT_PRIORITY + " TEXT, " +
                    STUDENT_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    " ) ";





    public DBopenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_STUDENT);
        onCreate(db);

    }
}
