package com.example.mycontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "EMPLOYEES";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";

    // Database Information
    static final String DB_NAME = "myEmployees.DB";

    // database version
    static final int DB_VERSION = 1;

    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + ADDRESS + " CHAR(50));";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public long add(ContentValues contentValue) {
        return database.insert(TABLE_NAME, null, contentValue);
    }

    public Cursor query(String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = database.query(TABLE_NAME, projection, selection,
                selectionArgs, null, null, sortOrder);

        return cursor;
    }

    public int update(ContentValues contentValues, String selection, String[] selectionArgs) {

        int count = database.update(TABLE_NAME, contentValues, selection, selectionArgs);
        return count;
    }

    public int delete(String selection, String[] selectionArgs) {
        return database.delete(TABLE_NAME, selection, selectionArgs);
    }
}
