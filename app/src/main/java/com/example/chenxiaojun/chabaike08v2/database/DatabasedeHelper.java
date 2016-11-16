package com.example.chenxiaojun.chabaike08v2.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by my on 2016/11/14.
 */
public class DatabasedeHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "my_database.db";
    private static final int DB_VERSION = 2;
    public static final String TABLE_NAME_SHOU = "shouChang";
    public static final String TABLE_NAME_JI = "jiLu";
    public static final String TABLE_NAME_HUAN = "huanCun";
    public DatabasedeHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表语句
        String sql1 = "create table if not exists " + TABLE_NAME_SHOU + "(_id integer primary key autoincrement, title varchar, wap_thumb varchar, source varchar, create_time varchar,id integer)";
        //执行语句
        db.execSQL(sql1);
        String sql2 = "create table if not exists " + TABLE_NAME_JI + "(_id integer primary key autoincrement, title varchar, wap_thumb varchar, source varchar, create_time varchar,id integer)";
        //执行语句
        db.execSQL(sql2);
        String sql3 = "create table if not exists " + TABLE_NAME_HUAN + "(_id integer primary key autoincrement, title varchar, wap_thumb varchar, source varchar, create_time varchar,id integer)";
        //执行语句
        db.execSQL(sql3);
        //db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS shouChang");
        db.execSQL("DROP TABLE IF EXISTS jiLu");
        db.execSQL("DROP TABLE IF EXISTS huanCun");
        onCreate(db);
    }
}
