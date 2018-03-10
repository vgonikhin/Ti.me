package ru.gb.android.time;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper { //Class with database structure

    //Constants
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "timers.db";

    //Table and column names
    static final String TABLE_TIMERS = "timers";
    static final String COLUMN_ID = "id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_START_HOURS = "start_hours";
    static final String COLUMN_START_MINUTES = "start_minutes";
    static final String COLUMN_START_SECONDS = "start_seconds";
    static final String COLUMN_CURRENT_HOURS = "current_hours";
    static final String COLUMN_CURRENT_MINUTES = "current_minutes";
    static final String COLUMN_CURRENT_SECONDS = "current_seconds";
    static final String COLUMN_TICKING = "ticking";

    //Constructor
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create new table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TIMERS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME + " TEXT, "
                        + COLUMN_TICKING + " NUMBER, "
                        + COLUMN_START_HOURS + " NUMBER, "
                        + COLUMN_START_MINUTES + " NUMBER, "
                        + COLUMN_START_SECONDS + " NUMBER,"
                        + COLUMN_CURRENT_HOURS + " NUMBER,"
                        + COLUMN_CURRENT_MINUTES + " NUMBER,"
                        + COLUMN_CURRENT_SECONDS + " NUMBER);");
    }

    //Update table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Nothing to do here
    }
}
