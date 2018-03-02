package ru.gb.android.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TimerDataSource {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_START_HOURS,
            DatabaseHelper.COLUMN_START_MINUTES,
            DatabaseHelper.COLUMN_START_SECONDS
    };

    public TimerDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
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

        database.insert(DatabaseHelper.TABLE_TIMERS, null, values);
    }

    public void editTimer(long id, String name, int startHours, int startMinutes, int startSeconds) {
        ContentValues editedTimer = new ContentValues();
        editedTimer.put(DatabaseHelper.COLUMN_ID, id);
        editedTimer.put(DatabaseHelper.COLUMN_NAME, name);
        editedTimer.put(DatabaseHelper.COLUMN_START_HOURS, startHours);
        editedTimer.put(DatabaseHelper.COLUMN_START_MINUTES, startMinutes);
        editedTimer.put(DatabaseHelper.COLUMN_START_SECONDS, startSeconds);

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

    public List<Timer> getAllTimers() {
        List<Timer> timers = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_TIMERS,
                notesAllColumn, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Timer timer = cursorToTimer(cursor);
            timers.add(timer);
            cursor.moveToNext();
        }
        cursor.close();
        return timers;
    }

    private Timer cursorToTimer(Cursor cursor) {
        return new Timer(cursor.getInt(0), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(1));
    }

}
