package ru.gb.android.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TimerDataSource {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private String[] timersAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_START_HOURS,
            DatabaseHelper.COLUMN_START_MINUTES,
            DatabaseHelper.COLUMN_START_SECONDS,
            DatabaseHelper.COLUMN_CURRENT_HOURS,
            DatabaseHelper.COLUMN_CURRENT_MINUTES,
            DatabaseHelper.COLUMN_CURRENT_SECONDS,
            DatabaseHelper.COLUMN_TICKING
    };

    private String[] timersIdColumn = {
            DatabaseHelper.COLUMN_ID
    };

    public TimerDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public boolean isOpen(){
        return database.isOpen();
    }

    public void close() {
        dbHelper.close();
    }

    public void addTimer(String name, int startHours, int startMinutes, int startSeconds) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_START_HOURS, startHours);
        values.put(DatabaseHelper.COLUMN_START_MINUTES, startMinutes);
        values.put(DatabaseHelper.COLUMN_START_SECONDS, startSeconds);
        values.put(DatabaseHelper.COLUMN_CURRENT_HOURS,startHours);
        values.put(DatabaseHelper.COLUMN_CURRENT_MINUTES,startMinutes);
        values.put(DatabaseHelper.COLUMN_CURRENT_SECONDS,startSeconds);
        values.put(DatabaseHelper.COLUMN_TICKING,0);

        database.insert(DatabaseHelper.TABLE_TIMERS, null, values);
    }

    public void editTimer(long id, String name, int startHours, int startMinutes, int startSeconds) {
        ContentValues editedTimer = new ContentValues();
        editedTimer.put(DatabaseHelper.COLUMN_ID, id);
        editedTimer.put(DatabaseHelper.COLUMN_NAME, name);
        editedTimer.put(DatabaseHelper.COLUMN_START_HOURS, startHours);
        editedTimer.put(DatabaseHelper.COLUMN_START_MINUTES, startMinutes);
        editedTimer.put(DatabaseHelper.COLUMN_START_SECONDS, startSeconds);
        editedTimer.put(DatabaseHelper.COLUMN_CURRENT_HOURS,startHours);
        editedTimer.put(DatabaseHelper.COLUMN_CURRENT_MINUTES,startMinutes);
        editedTimer.put(DatabaseHelper.COLUMN_CURRENT_SECONDS,startSeconds);
        editedTimer.put(DatabaseHelper.COLUMN_TICKING,0);

        database.update(DatabaseHelper.TABLE_TIMERS,
                editedTimer,
                DatabaseHelper.COLUMN_ID + "=" + id,
                null);
    }

    public void editTimer(long id, int ticking) {
        ContentValues editedTimer = new ContentValues();
        editedTimer.put(DatabaseHelper.COLUMN_ID, id);
        editedTimer.put(DatabaseHelper.COLUMN_TICKING,ticking);

        database.update(DatabaseHelper.TABLE_TIMERS,
                editedTimer,
                DatabaseHelper.COLUMN_ID + "=" + id,
                null);
    }

    public void deleteTimer(long id) {
        database.delete(DatabaseHelper.TABLE_TIMERS, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAll() {
        database.delete(DatabaseHelper.TABLE_TIMERS, null, null);
    }

    public List<TiMeTimer> getAllTimers() {
        List<TiMeTimer> timers = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_TIMERS,
                timersAllColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TiMeTimer tiMeTimer = cursorToTimer(cursor);
            timers.add(tiMeTimer);
            cursor.moveToNext();
        }
        cursor.close();
        return timers;
    }

    private TiMeTimer cursorToTimer(Cursor cursor) {
        return new TiMeTimer(cursor.getInt(0), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(1),cursor.getInt(8));
    }

    public int getNextId(){
        Cursor cursor = database.query(DatabaseHelper.TABLE_TIMERS,timersIdColumn,null,null,null,null,"id desc", "1");
        cursor.moveToFirst();
        int lastId = cursor.getInt(0);
        Log.w("ID", "getNextId: "+lastId);
        cursor.close();
        return lastId+1;
    }
}
