package com.example.plantkoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AccountDb
{
    public static final String TAG = "AccountDb";
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] AllColumns =
    {
        DBConn.AccountDatabase.COLUMN_ACCOUNT_ID,
        DBConn.AccountDatabase.COLUMN_ACCOUNT_IMAGE,
        DBConn.AccountDatabase.COLUMN_ACCOUNT_FULLNAME,
        DBConn.AccountDatabase.COLUMN_ACCOUNT_USERNAME,
        DBConn.AccountDatabase.COLUMN_ACCOUNT_EMAIL,
        DBConn.AccountDatabase.COLUMN_ACCOUNT_PASSWORD,
        DBConn.AccountDatabase.COLUMN_ACCOUNT_LOCATION
    };

    public AccountDb(Context context)
    {
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        // open the database
        try {
            open();
        } catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException
    {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public Account createAccount(byte[] accountPic, String name, String username, String email,
                                 String password, String location)
    {
        ContentValues values = new ContentValues();
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_IMAGE, accountPic);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_FULLNAME, name);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_USERNAME, username);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_EMAIL, email);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_PASSWORD, password);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_LOCATION, location);

        long insertAccID = mDatabase.insert(DBConn.AccountDatabase.ACCOUNT_TB, null,values);

        Cursor cursor = mDatabase.query(DBConn.AccountDatabase.ACCOUNT_TB, AllColumns,
                DBConn.AccountDatabase.COLUMN_ACCOUNT_ID + " = " + insertAccID,
                null, null,null,null);

        cursor.moveToNext();
        Account newAccount = CursorToAccount(cursor);
        cursor.close();
        return newAccount;
    }

    public Account GetAccountId(long id)
    {
        Cursor cursor = mDatabase.query(DBConn.AccountDatabase.ACCOUNT_TB, AllColumns,
                DBConn.AccountDatabase.COLUMN_ACCOUNT_ID + " = ? ",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Account accounts = CursorToAccount(cursor);
        return accounts;
    }

//    public Account GetAccountLocation(long id)
//    {
//        Cursor cursor = mDatabase.query(DBConn.AccountDatabase.ACCOUNT_TB, AllColumns,
//                DBConn.AccountDatabase.COLUMN_ACCOUNT_LOCATION + " = ? ",
//                new String[] { String.valueOf(id) }, null, null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//
//        Account accounts = CursorToAccount(cursor);
//        return accounts;
//    }

    protected Account CursorToAccount(Cursor cursor)
    {
        Account account = new Account();
        account.setAccountId(cursor.getLong(0));
        account.setbyteUserPofilePic(cursor.getBlob(1));
        account.setFullName(cursor.getString(2));
        account.setUsername(cursor.getString(3));
        account.setEmail(cursor.getString(4));
        account.setPassword(cursor.getString(5));
        account.setLocation(cursor.getString(6));

        return account;
    }

    public boolean CheckUser(String username, String password)
    {

        // array of columns to fetch
        String[] columns = {
                DBConn.AccountDatabase.COLUMN_ACCOUNT_ID
        };
        // selection criteria
        String selection = DBConn.AccountDatabase.COLUMN_ACCOUNT_USERNAME + " = ?" + " AND " + DBConn.AccountDatabase.COLUMN_ACCOUNT_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {username, password};
        // query user table with conditions
        Cursor cursor = mDatabase.query(DBConn.AccountDatabase.ACCOUNT_TB, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public long Sender(String activeuser)
    {
        // array of columns to fetch
        long rv = -1;
        mDatabase =  mDbHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query(DBConn.AccountDatabase.ACCOUNT_TB, AllColumns,
                DBConn.AccountDatabase.COLUMN_ACCOUNT_USERNAME + " = ? ",
                new String[] { activeuser }, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                rv = cursor.getLong(cursor.getColumnIndex(DBConn.AccountDatabase.COLUMN_ACCOUNT_ID));
            }
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return rv;
    }// End oF Curly Braces Sender

    public void UpdateAccount(long getId, byte[] profilepicAccount, String name, String username, String email,
                              String password,String location)
    {
        ContentValues values = new ContentValues();
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_IMAGE, profilepicAccount);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_FULLNAME, name);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_USERNAME, username);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_EMAIL, email);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_PASSWORD, password);
        values.put(DBConn.AccountDatabase.COLUMN_ACCOUNT_LOCATION, location);

        mDatabase.update(DBConn.AccountDatabase.ACCOUNT_TB, values, DBConn.AccountDatabase.COLUMN_ACCOUNT_ID + " = ?",
                new String[]{String.valueOf(getId)});
    }

}
