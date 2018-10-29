package com.tools.QRCodeReader.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tools.QRCodeReader.bean.DataModel;

import java.util.ArrayList;


/**
 * Created by ${张志珍} on 2016/12/27
 * 17:47.
 */

public class DatabaseDao {

    private static final String TAG = DatabaseDao.class.getSimpleName();
    private DatabaseHelper dbHelper;

    public DatabaseDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * 添加数据
     */
    public void insert(DataModel model) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //插入数据SQL语句
        String sql = "insert into qrlist(url,time)values(?,?)";
        Object[] args = {model.getUrl()};
        db.execSQL(sql, args);
        db.close();
    }




    /**
     * 删除表数据
     */
    public void removeTableData() {
        SQLiteDatabase db =null;
        try {
            db = dbHelper.getWritableDatabase();
            String sql = "delete from qrlist";
            db.execSQL(sql);
        }catch (Exception e){
            db.close();
        }finally {
            db.close();
            if(db!=null){
                //清除对象
                db=null;
            }
        }
    }

    /**
     * 删除数据
     *
     * @param url
     */
    public void deleteByUrl(String url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "delete from qrlist where url = ?";
        Object[] args = {url};
        db.execSQL(sql, args);
        db.close();
    }



    /**
     * 查询所有
     *
     * @return
     */
    public ArrayList<DataModel> queryAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select * from qrlist order by time asc";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<DataModel> music_tables = new ArrayList<DataModel>();
        DataModel model = null;
        while (cursor.moveToNext()) {
            model = new DataModel();
            if (cursor != null && cursor.getCount() != 0) {
                model.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            }
            music_tables.add(model);
        }
        if (cursor != null) {
            cursor.close();
        }
        return music_tables;
    }

    //根据语言查询数据
    public ArrayList<DataModel> queryByUrl(String url) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<DataModel> list = new ArrayList<DataModel>();
        String sql="select * from qrlist where url='" + url +"'";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<DataModel> listdata = new ArrayList<DataModel>();
        DataModel model = null;
        while (cursor.moveToNext()) {
            model = new DataModel();
            if (cursor != null && cursor.getCount() != 0) {
                model.setUrl(cursor.getString(cursor.getColumnIndex("url")));

            }
            listdata.add(model);
        }
        if (cursor != null) {
            cursor.close();
        }
        return listdata;
    }



}
