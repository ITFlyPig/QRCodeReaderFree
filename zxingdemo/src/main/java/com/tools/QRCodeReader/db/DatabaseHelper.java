package com.tools.QRCodeReader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ${张志珍} on 2016/12/27
 * Project_NameDemoMusic
 * Package_Namecom.example.amd.demomusic.db
 * 17:39.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //数据库名字
    private static final String DB_NAME = "zzzdata.db";

    //本版号
    private static final int DB_VERSION = 1;

    //创建表
    public static final String CREATE_TABLE_MUSIC = "create table qrlist ("

            + "_id integer primary key autoincrement, url text,time long)";


    //删除表
    private static final String DROP_TABLE_NOTE = "drop table if exists qrlist";




    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQLiteDatabase 用于操作数据库的工具类
        db.execSQL(CREATE_TABLE_MUSIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_MUSIC);
    }

}
