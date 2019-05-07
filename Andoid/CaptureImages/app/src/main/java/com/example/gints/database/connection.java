package com.example.gints.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class connection extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LeafDb.db";
    public static final String LEAF_TABLE_NAME = "leaf_info";
    public static final String LEAF_COLUMN_ID = "id";
    public static final String LEAF_COLUMN_NAME = "nosaukums";
    public static final String LEAF_COLUMN_COUNTRY = "valsts";
    public static final String LEAF_COLUMN_TYPE = "tips";
    public static final String LEAF_COLUMN_DESCRIPTION = "apraksts";
    public static final String LEAF_COLUMN_LEAFCODE = "kods";

    private HashMap hp;

    public connection(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                "create table leaf_info " +
                        "( "+LEAF_COLUMN_ID+ "   integer primary key, nosaukums text,valsts text,tips text, apraksts text,kods text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS leaf_info");
        onCreate(db);
    }

    public boolean insert_leaf (String nosaukums, String valsts, String tips, String apraksts,String kods) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nosaukums", nosaukums);
        contentValues.put("valsts", valsts);
        contentValues.put("tips", tips);
        contentValues.put("apraksts", apraksts);
        contentValues.put("kods", kods);
        db.insert("leaf_info", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from leaf_info where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LEAF_TABLE_NAME);
        return numRows;
    }

    public boolean updateLeaf(Integer id, String nosaukums, String valsts, String tips, String apraksts,String kods) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nosaukums", nosaukums);
        contentValues.put("valsts", valsts);
        contentValues.put("tips", tips);
        contentValues.put("apraksts", apraksts);
        contentValues.put("kods", kods);
        db.update("leaf_info", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteLeaf (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("leaf_info",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllLeafs(String kods) {
        ArrayList<String> array_list = new ArrayList<String>();

       // String countQuery = "SELECT  * FROM " + LEAF_TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + LEAF_TABLE_NAME  , null);

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            array_list.add(cursor.getString(cursor.getColumnIndex(LEAF_COLUMN_ID)));
            cursor.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllDataByColumName(String column,String value) {

        ArrayList<String> array_nosaukums = new ArrayList<String>();
        ArrayList<String> array_valsts = new ArrayList<String>();
        ArrayList<String> array_tips = new ArrayList<String>();
        ArrayList<String> array_apraksts = new ArrayList<String>();



        // String countQuery = "SELECT  * FROM " + LEAF_TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT *  FROM " + LEAF_TABLE_NAME + " where " + "kods" +" = " + "'" + value + "'" , null);

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            array_nosaukums.add(cursor.getString(1));
            array_valsts.add(cursor.getString(2));
            array_tips.add(cursor.getString(3));
            array_apraksts.add(cursor.getString(4));

            cursor.moveToNext();

        }
        ArrayList<String> all_data = new ArrayList<String>();
        all_data.addAll(array_nosaukums);
        all_data.addAll(array_valsts);
        all_data.addAll(array_tips);
        all_data.addAll(array_apraksts);
        return  all_data;
    }

}