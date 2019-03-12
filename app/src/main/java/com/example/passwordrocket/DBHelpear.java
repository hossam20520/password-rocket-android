package com.example.passwordrocket;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelpear extends SQLiteOpenHelper {
    public static final String DATABAS_NAME = "MyDB.db";
    public static final String TABLE_NAME = "contacts";
    public  static  final  String COLUMN_NAME = "name";
    public static  final  String ID = "id";
    public static  final String EMAIL = "email";
    public static  final String PASSWORDS = "passwords";
    private HashMap hp;

    public DBHelpear(Context context) {

        super(context, DATABAS_NAME , null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts (id integer primary key autoincrement , name text , passwords text, email text )"
        );

        db.execSQL(
                "create table user (id integer primary key autoincrement , username text , password text)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    public  boolean insertContent(String name, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("passwords", password);
        db.insert("contacts", null , contentValues);
        return  true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where id="+id, null);
        //StringBuffer buffer = new StringBuffer();

        return res;
    }


    public int get_name(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where name LIKE "+"'"+name+"%'", null);

        return res.getCount();

    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        return  numRows;
    }

    public  boolean updateContent (Integer id, String name, String email , String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("passwords", password);
        db.update("contacts" , contentValues , "id = ?" , new String[] {Integer.toString(id)});
        return true;
    }


    public String Update(String name ,String  email ,String  password , String new_txt ){
        SQLiteDatabase db = this.getWritableDatabase();
//     ContentValues contentValuesx = new ContentValues();
//     contentValuesx.put("name", name);
//       contentValuesx.put("email", email);
//     contentValuesx.put("passwords", password);
//       db.update("contacts" , contentValuesx , COLUMN_NAME+"="+"'"+name+"'" , null);\

        String sqll = "update contacts set name ="+"'"+new_txt+"',email ="+"'"+email+"',passwords ="+"'"+password+"' where name="+"'"+name+"'";
        db.execSQL(sqll);

        //Cursor res = db.rawQuery( "update contacts set name ='googleU' , email = 'hossam@gmail.com' where name ='google' ", null);
       // Cursor res = db.rawQuery( "update contacts set name ="+"'"+name+"' , email ="+"'"+email+"' , passwords ="+"'"+password+"' where name ="+"'"+name+"'", null);
//
//
// db.execSQL(
//                "update contacts set name ="+"'"+name+"' , email ="+"'"+email+"' , passwords ="+"'"+password+"' where name ="+"'"+name+"'"
//        );
        return  sqll;
    }

    public ArrayList<String> getAllContacts(){
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts" , null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }

        return arrayList;
    }

    public boolean delete(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqll = "delete from contacts where name="+"'"+name+"'";
        db.execSQL(sqll);
        return  true;
    }

    public ArrayList<String> getSingleContent(String name){
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where name ="+"'"+name+"'", null);
        res.moveToFirst();
        arrayList.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
        arrayList.add(res.getString(res.getColumnIndex(EMAIL)));
        arrayList.add(res.getString(res.getColumnIndex(PASSWORDS)));
        return  arrayList;
    }





}

