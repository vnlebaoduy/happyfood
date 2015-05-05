package com.teamap.mydemo.DataBaseHandler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.teamap.mydemo.Entity.tblDanhmuc;
import com.teamap.mydemo.Entity.tblQuan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBController extends SQLiteOpenHelper {

    public DBController(Context applicationcontext) {
        super(applicationcontext, "Data2.db", null, 1);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query,query2;
        query = "CREATE TABLE tblDanhmuc ( PK_IDDanhmuc INTEGER, tendanhmuc TEXT)";
        query2 = "CREATE TABLE tblquan ( PK_IDQuan INTEGER, tenquan TEXT, diachi TEXT, toado TEXT, daden INTEGER, yeuthich INTEGER, mota TEXT,anhdaidien TEXT, monnoibat TEXT,FK_IDDanhmuc INTEGER)";

        database.execSQL(query);
        database.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS tblDanhmuc";
        database.execSQL(query);
        onCreate(database);
    }


    /**
     * Inserts User into SQLite DB
     *
     * @param queryValues
     */
    /*public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", queryValues.get("userId"));
        values.put("userName", queryValues.get("userName"));
        database.insert("users", null, values);
        database.close();
    }
*/

    /**
     * Get list of Users from SQLite DB as Array List
     *
     * @return
     */
    public List<tblDanhmuc> getAllDanhmuc() {

        List<tblDanhmuc> danhmucList = new ArrayList<tblDanhmuc>();
        String selectQuery = "SELECT  * FROM tbldanhmuc";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tblDanhmuc dm = new tblDanhmuc();
                dm.setPK_IDDanhmuc(cursor.getInt(0));
                dm.setTendanhmuc(cursor.getString(1));

                danhmucList.add(dm);
            } while (cursor.moveToNext());
        }
        database.close();
        return danhmucList;
    }


    public void insertDanhmuc(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PK_IDDanhmuc", queryValues.get("PK_IDDanhmuc"));
        values.put("tendanhmuc", queryValues.get("tendanhmuc"));
        database.insert("tbldanhmuc", null, values);
        database.close();
    }


    public List<tblQuan> getAllQuan() {
        List<tblQuan> quanList = new ArrayList<tblQuan>();
        String selectQuery = "SELECT  * FROM tblQuan";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tblQuan quan = new tblQuan();
                quan.setPK_IDQuan(cursor.getInt(0));
                quan.setTenquan(cursor.getString(1));
                quan.setDiachi(cursor.getString(2));
                quan.setToado(cursor.getString(3));
                quan.setDaden(cursor.getInt(4));
                quan.setYeuthich(cursor.getInt(5));
                quan.setMota(cursor.getString(6));
                quan.setAnhdaidien(cursor.getString(7));
                quan.setMonnoibat(cursor.getString(8));
                quan.setFK_IDDanhmuc(cursor.getInt(9));

                quanList.add(quan);
            } while (cursor.moveToNext());
        }
        database.close();
        return quanList;
    }

    public void insertQuan(tblQuan quans) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PK_IDQuan", quans.getPK_IDQuan());
        values.put("tenquan", quans.getTenquan());
        values.put("diachi", quans.getDiachi());
        values.put("toado", quans.getToado());
        values.put("daden", quans.getDaden());
        values.put("yeuthich", quans.getYeuthich());
        values.put("mota", quans.getMota());
        values.put("anhdaidien", quans.getAnhdaidien());
        values.put("monnoibat", quans.getMonnoibat());
        values.put("FK_IDDanhmuc", quans.getFK_IDDanhmuc());
        database.insert("tblquan", null, values);
        database.close();
    }

    public void removeQuan  (){
        try {
            String del = "DELETE FROM tblQuan";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(del);
            db.close();
        } catch (Exception e) {
        }

    }

    public void removeDM  (){
        try {
            String del = "DELETE FROM tbldanhmuc";
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(del);
            db.close();
        } catch (Exception e) {
        }

    }

}
