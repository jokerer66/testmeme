package com.instanza.testmemo.DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.instanza.testmemo.Bean.MemoListItem;
import com.instanza.testmemo.DB.SQLiteHelper.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2017/9/30.
 */

public class MemoDao {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
    public MemoDao(Context context) {
        mySQLiteHelper = new MySQLiteHelper(context,"memo.db",null,0);
//        db = mySQLiteHelper.getReadableDatabase();
//        sp = context.getSharedPreferences("memoitem",Context.MODE_APPEND);
//        editor = sp.edit();
    }

    public void execMemoSQL(String sql){
        db = mySQLiteHelper.getReadableDatabase();
        if(db == null){
            return ;
        }
        db.execSQL(sql);
        db.close();

    }

    public List<MemoListItem> selectMemoBySql(String sql){
        db = mySQLiteHelper.getReadableDatabase();
        if(db == null){
            return null;
        }
        Cursor cursor ;
        if(sql == "" || sql ==null){
            cursor = db.rawQuery("select * from memo", null);
        }else{
            cursor = db.rawQuery("select * from memo "+sql , null);
        }

        List<MemoListItem> memolist = new ArrayList<>();
        while (cursor.moveToNext()) {
            MemoListItem item = new MemoListItem(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            memolist.add(item);
        }

        cursor.close();

        db.close();
        return memolist;
    }
}
