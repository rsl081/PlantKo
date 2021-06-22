package com.example.plantkoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PlantDb {
    public static final String TAG = "PlantDb";

    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns =
    {
        DBConn.PlantDatabase.COLUMN_PLANT_ID,
        DBConn.PlantDatabase.COLUMN_PLANT_IMAGE,
        DBConn.PlantDatabase.COLUMN_PLANT_NAME,
        DBConn.PlantDatabase.COLUMN_PLANT_CATEGORY,
        DBConn.PlantDatabase.COLUMN_PLANT_DESCRIPTION,
        DBConn.PlantDatabase.COLUMN_PLANT_DATE,
        DBConn.PlantDatabase.COLUMN_PLANT_TIME,
        DBConn.PlantDatabase.COLUMN_PLANT_ACCOUNT_ID
    };

    public PlantDb(Context context) {
        mDbHelper = new DBHelper(context);
        this.mContext = context;
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Plant createdPlant(byte[] profilePicPlant, String plantname, String category, String description,
                              String date, String time, long accountIDPlant)
    {
        ContentValues values = new ContentValues();
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_IMAGE, profilePicPlant);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_NAME, plantname);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_CATEGORY, category);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_DESCRIPTION, description);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_DATE, date);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_TIME, time);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_ACCOUNT_ID, accountIDPlant);

        long insertId = mDatabase.insert(DBConn.PlantDatabase.PLANT_TB, null, values);

        Cursor cursor = mDatabase.query(DBConn.PlantDatabase.PLANT_TB, mAllColumns,
                DBConn.PlantDatabase.COLUMN_PLANT_ID + " = " + insertId, null, null,
                null, null);

        cursor.moveToFirst();
        Plant newPlant = CursorToPlant(cursor);
        cursor.close();
        return newPlant;
    }

    public List<Plant> getAllPeople(long companyId) {
        List<Plant> personArrayList = new ArrayList<Plant>();

        Cursor cursor = mDatabase.query(DBConn.PlantDatabase.PLANT_TB, mAllColumns,
                DBConn.PlantDatabase.COLUMN_PLANT_ACCOUNT_ID + " = ?",
                new String[] { String.valueOf(companyId) }, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Plant plant = CursorToPlant(cursor);
            personArrayList.add(plant);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return personArrayList;
    }

    private Plant CursorToPlant(Cursor cursor)
    {
        Plant plant = new Plant();
        plant.setIdplant(cursor.getLong(0));
        plant.setPlantbyteProfilePic(cursor.getBlob(1));
        plant.setPlantName(cursor.getString(2));
        plant.setPlantCategory(cursor.getString(3));
        plant.setPlantDescription(cursor.getString(4));
        plant.setPlantDate(cursor.getString(5));
        plant.setPlantTime(cursor.getString(6));

        return plant;
    }

    public boolean DeletePlant(long plantId) {
        //long id = person.getId();
        return mDatabase.delete(DBConn.PlantDatabase.PLANT_TB,
                DBConn.PlantDatabase.COLUMN_PLANT_ID + "=" + plantId,
                null) > 0;
    }//end of Curly Brace deletePerson

    public void UpdatePlant(
            long getId, byte[] plantPic, String plantName, String category, String description,
            String date, String time)
    {
        ContentValues values = new ContentValues();
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_IMAGE, plantPic);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_NAME, plantName);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_CATEGORY, category);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_DESCRIPTION, description);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_DATE, date);
        values.put(DBConn.PlantDatabase.COLUMN_PLANT_TIME, time);

        mDatabase.update(DBConn.PlantDatabase.PLANT_TB, values,
                DBConn.PlantDatabase.COLUMN_PLANT_ID + " = ?",
                new String[]{String.valueOf(getId)});
    }

    public long SenderPlant(String activeuser){
        // array of columns to fetch
        long rv = -1;
        mDatabase =  mDbHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(DBConn.PlantDatabase.PLANT_TB, mAllColumns,
                DBConn.PlantDatabase.COLUMN_PLANT_NAME + " = ? ",
                new String[] { activeuser }, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                rv = cursor.getLong(cursor.getColumnIndex(DBConn.PlantDatabase.COLUMN_PLANT_ID));
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return rv;
    }// End oF Curly Braces Sender
}
