package com.example.plantkoapp;

import android.database.Cursor;
import android.provider.BaseColumns;

public class DBConn
{
    public static class AccountDatabase implements BaseColumns
    {
        //registration table
        public static String ACCOUNT_TB = "table_account";
        //column name
        public static String COLUMN_ACCOUNT_ID = "id_account";
        public static String COLUMN_ACCOUNT_IMAGE = "image_account";
        public static String COLUMN_ACCOUNT_FULLNAME = "fullname_account";
        public static String COLUMN_ACCOUNT_USERNAME = "username_account";
        public static String COLUMN_ACCOUNT_EMAIL = "email_account";
        public static String COLUMN_ACCOUNT_PASSWORD = "password_account";
        public static String COLUMN_ACCOUNT_LOCATION = "location_account";
    }

    public static class PlantDatabase implements BaseColumns
    {
        //plant table
        public static String PLANT_TB = "table_plant";
        //column name
        public static String COLUMN_PLANT_ID = "id_plant";
        public static String COLUMN_PLANT_IMAGE = "image_plant";
        public static String COLUMN_PLANT_NAME = "name_plant";
        public static String COLUMN_PLANT_CATEGORY = "category_plant";
        public static String COLUMN_PLANT_DESCRIPTION = "description_plant";
        public static String COLUMN_PLANT_DATE = "date_plant";
        public static String COLUMN_PLANT_TIME = "time_plant";
        public static String COLUMN_PLANT_ALARM_TIME = "alarm_time_plant";
        public static String COLUMN_PLANT_ACCOUNT_ID = "account_plant";
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

}
