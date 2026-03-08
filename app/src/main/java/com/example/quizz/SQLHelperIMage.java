package com.example.quizz;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelperIMage extends SQLiteOpenHelper {

        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "imagenUsuario";

        // Table Names
        private static final String DB_TABLE = "table_image";

        // column names
        private static final String KEY_NAME = "image_name";
        private static final String KEY_IMAGE = "image_data";

        // Table create statement
        private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
                KEY_NAME + " TEXT," +
                KEY_IMAGE + " BLOB);";

        public SQLHelperIMage(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // creating table
            db.execSQL(CREATE_TABLE_IMAGE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // on upgrade drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

            // create new table
            onCreate(db);
        }
    }

