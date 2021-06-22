package com.example.plantkoapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{

    final private static String DATABASE_NAME = "MyDatabase.db";
    final private static int VERSION = 1;
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CREATE_ACCOUNT_TABLE = "CREATE TABLE '"+DBConn.AccountDatabase.ACCOUNT_TB+
                "' (" + " '"+DBConn.AccountDatabase.COLUMN_ACCOUNT_ID+"' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+DBConn.AccountDatabase.COLUMN_ACCOUNT_IMAGE+"'BLOB NOT NULL,"+
                "'"+DBConn.AccountDatabase.COLUMN_ACCOUNT_FULLNAME+"'TEXT NOT NULL,"+
                "'"+DBConn.AccountDatabase.COLUMN_ACCOUNT_USERNAME+"'TEXT NOT NULL," +
                "'"+DBConn.AccountDatabase.COLUMN_ACCOUNT_EMAIL+"'TEXT NOT NULL," +
                "'"+DBConn.AccountDatabase.COLUMN_ACCOUNT_PASSWORD+"'DATE NOT NULL," +
                "'"+DBConn.AccountDatabase.COLUMN_ACCOUNT_LOCATION+"'TEXT NOT NULL)";

        final String CREATE_PLANT_TABLE = "CREATE TABLE '"+DBConn.PlantDatabase.PLANT_TB+
                "' (" + " '"+DBConn.PlantDatabase.COLUMN_PLANT_ID+"' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_IMAGE+"'BLOB NOT NULL,"+
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_NAME+"'TEXT NOT NULL,"+
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_CATEGORY+"'TEXT NOT NULL," +
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_DESCRIPTION+"'TEXT NOT NULL," +
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_DATE+"'DATE NOT NULL," +
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_TIME+"'TEXT NOT NULL," +
                "'"+DBConn.PlantDatabase.COLUMN_PLANT_ACCOUNT_ID+"'INTEGER NOT NULL)";

        //Execute Database!
        try {
            db.execSQL(CREATE_ACCOUNT_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL(CREATE_PLANT_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
