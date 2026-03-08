package com.example.quizz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper
{
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase myBBDD){
        myBBDD.execSQL("create table preguntas(numero int primary key, opcion1 text, opcion2 text, opcion3 text, opcion4 text, opcion5 text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + "preguntas");
       onCreate(sqLiteDatabase);
    }
}
