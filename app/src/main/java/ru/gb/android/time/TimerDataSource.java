package ru.gb.android.time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

class TimerDataSource { //Class for working with database

    //Support elements
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    //All columns
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

    //Column 'id'
    private String[] timersIdColumn = {
            DatabaseHelper.COLUMN_ID
    };

    //Constructor
    TimerDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //Methods
    //Open database
    void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    //Check database state
    boolean isOpen(){
        return database.isOpen();
    }

    //Close database
    void close() {
        dbHelper.close();
    }

    //Insert new row
    void addTimer(String name, int startHours, int startMinutes, int startSeconds) {
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

    //Edit all columns in a row
    void editTimer(TiMeTimer timer) {
        ContentValues editedTimer = new ContentValues();
        editedTimer.put(DatabaseHelper.COLUMN_ID, timer.getId());
        editedTimer.put(DatabaseHelper.COLUMN_NAME, timer.getName());
        editedTimer.put(DatabaseHelper.COLUMN_START_HOURS, timer.getStartHours());
        editedTimer.put(DatabaseHelper.COLUMN_START_MINUTES, timer.getStartMinutes());
        editedTimer.put(DatabaseHelper.COLUMN_START_SECONDS, timer.getStartSeconds());
        editedTimer.put(DatabaseHelper.COLUMN_CURRENT_HOURS, timer.getStartHours());
        editedTimer.put(DatabaseHelper.COLUMN_CURRENT_MINUTES, timer.getStartMinutes());
        editedTimer.put(DatabaseHelper.COLUMN_CURRENT_SECONDS, timer.getStartSeconds());
        editedTimer.put(DatabaseHelper.COLUMN_TICKING, 0);

        database.update(DatabaseHelper.TABLE_TIMERS, editedTimer, DatabaseHelper.COLUMN_ID + "=" + timer.getId(), null);
    }

    //Edit column 'ticking' in a row
    void editTimer(long id, int ticking) {
        ContentValues editedTimer = new ContentValues();
        editedTimer.put(DatabaseHelper.COLUMN_ID, id);
        editedTimer.put(DatabaseHelper.COLUMN_TICKING, ticking);

        database.update(DatabaseHelper.TABLE_TIMERS, editedTimer, DatabaseHelper.COLUMN_ID + "=" + id, null);
    }

    //Delete one row
    void deleteTimer(TiMeTimer timer) {
        database.delete(DatabaseHelper.TABLE_TIMERS, DatabaseHelper.COLUMN_ID + " = " + timer.getId(), null);
    }

    //Delete all rows
    void deleteAll() {
        database.delete(DatabaseHelper.TABLE_TIMERS, null, null);
    }

    //Read all timers from database
    List<TiMeTimer> getAllTimers() {
        List<TiMeTimer> timers = new ArrayList<>(); //Prepare list

        Cursor cursor = database.query(DatabaseHelper.TABLE_TIMERS, timersAllColumn, null, null, null, null, null); //Query database
        cursor.moveToFirst(); // Starting position
        while (!cursor.isAfterLast()) { //Cycle through all rows
            TiMeTimer tiMeTimer = cursorToTimer(cursor); //Instantiate timer from database row
            timers.add(tiMeTimer); //Add timer to list
            cursor.moveToNext();
        }
        cursor.close();
        return timers;
    }

    //Instantiate timer from database row
    private TiMeTimer cursorToTimer(Cursor cursor) {
        return new TiMeTimer(cursor.getInt(0), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(1),cursor.getInt(8));
    }

    //Determine the biggest id in the database
    int getNextId(){
        Cursor cursor = database.query(DatabaseHelper.TABLE_TIMERS,timersIdColumn,null,null,null,null,"id desc", "1"); //Query for id in the row with the biggest id
        cursor.moveToFirst(); //Starting position
        int lastId = cursor.getInt(0); //Get int value of the biggest id
        cursor.close();
        return lastId+1;
    }
}