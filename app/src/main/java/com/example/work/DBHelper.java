package com.example.work;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE USER(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username VARCHAR(20)," +
                "password VARCHAR(20)," +
                "gender INT," +
                "city VARCHAR(20)," +
                "birthday VARCHAR(20)," +
                "avatar BLOB" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
