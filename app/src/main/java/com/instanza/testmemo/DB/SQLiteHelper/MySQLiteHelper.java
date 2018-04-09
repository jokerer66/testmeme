package com.instanza.testmemo.DB.SQLiteHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apple on 2017/10/9.
 */

public class MySQLiteHelper extends SQLiteOpenHelper{

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "test", factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists memo ('id' INTEGER PRIMARY KEY ,`content` varchar(1000) NOT NULL,`createtime` varchar(200),'type','tag');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
