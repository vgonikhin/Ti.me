package ru.gb.android.time;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_START_HOURS = "start_hours";
    public static final String COLUMN_START_MINUTES = "start_minutes";
    public static final String COLUMN_START_SECONDS = "start_seconds";
    public static final String TABLE_TIMERS = "timers";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "timers.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_TIMERS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME + " TEXT, "
                        + COLUMN_START_HOURS + " NUMBER, "
                        + COLUMN_START_MINUTES + " NUMBER, "
                        + COLUMN_START_SECONDS + " NUMBER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
