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
    public static final String COLUMN_CURRENT_HOURS = "current_hours";
    public static final String COLUMN_CURRENT_MINUTES = "current_minutes";
    public static final String COLUMN_CURRENT_SECONDS = "current_seconds";
    public static final String COLUMN_TICKING = "ticking";
    public static final String TABLE_TIMERS = "timers";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "timers.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TIMERS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME + " TEXT, "
                        + COLUMN_TICKING + " NUMBER, "
                        + COLUMN_START_HOURS + " NUMBER, "
                        + COLUMN_START_MINUTES + " NUMBER, "
                        + COLUMN_START_SECONDS + " NUMBER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion==1 && newVersion==2) {
            db.execSQL("ALTER TABLE " + TABLE_TIMERS + " ADD "
                    + COLUMN_CURRENT_HOURS + " NUMBER;");
            db.execSQL("ALTER TABLE " + TABLE_TIMERS + " ADD "
                    + COLUMN_CURRENT_MINUTES + " NUMBER;");
            db.execSQL("ALTER TABLE " + TABLE_TIMERS + " ADD "
                    + COLUMN_CURRENT_SECONDS + " NUMBER;");
            db.execSQL("ALTER TABLE " + TABLE_TIMERS + " ADD "
                    + COLUMN_TICKING + " NUMBER;");
        }
    }
}
