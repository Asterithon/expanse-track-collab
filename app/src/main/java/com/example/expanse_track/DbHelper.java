package com.example.expanse_track;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "expense";
    public static final int DB_VERSION = 2;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE `transaction` (id INTEGER PRIMARY KEY AUTOINCREMENT, `amount` INT(11) NOT NULL , `description` VARCHAR(200) NOT NULL ,`date` VARCHAR(10) NOT NULL, `type` INT(1) NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "INSERT INTO `transaction`(`amount`, `description`, `date`, `type`) VALUES ('1000000','jual hp','6/12/2025','1')";
        db.execSQL(sql);
    }
}

