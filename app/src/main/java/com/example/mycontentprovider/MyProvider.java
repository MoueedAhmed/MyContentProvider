package com.example.mycontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.myprovidercontract.MyProviderContract;

public class MyProvider extends ContentProvider {

    private DatabaseHelper myHelper;

    // Creates a UriMatcher object.
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize.
         */

        sUriMatcher.addURI(MyProviderContract.AUTHORITY, MyProviderContract.BASE_PATH, 1);
        sUriMatcher.addURI(MyProviderContract.AUTHORITY, MyProviderContract.BASE_PATH + "/#", 2);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public boolean onCreate() {

        /*
         * Creates a new helper object. This method always returns quickly.
         * Notice that the database itself isn't created or opened
         * until SQLiteOpenHelper.getWritableDatabase is called
         */

        myHelper = new DatabaseHelper(getContext());

        return true;
    }

    // Implements ContentProvider.query()
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (sUriMatcher.match(uri)) {

            // If the incoming URI was for all of table3
            case 1:
                break;

            default:
                // If the URI is not recognized, you should do some error handling here
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        myHelper.open();

        Cursor cursor = myHelper.query(projection, selection, selectionArgs, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = 0;

        myHelper.open();

        switch (sUriMatcher.match(uri)) {

            // If the incoming URI was for all of table3
            case 1:
                id = myHelper.add(values);
                break;

            default:
                // If the URI is not recognized, you should do some error handling here
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(MyProviderContract.BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = 0;

        myHelper.open();

        switch (sUriMatcher.match(uri)) {

            // If the incoming URI has an id
            case 2:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = myHelper.delete(DatabaseHelper._ID + "=" + id, null);
                } else {
                    rowsDeleted = myHelper.delete(DatabaseHelper._ID + "=" + id + " and " + selection, selectionArgs);
                }

                break;

            default:
                // If the URI is not recognized, you should do some error handling here
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int rowsUpdated = 0;

        myHelper.open();

        switch (sUriMatcher.match(uri)) {

            // update all employees
            case 1:
                rowsUpdated = myHelper.update(values, selection, selectionArgs);

                // If the incoming URI has an id
            case 2:
                String id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = myHelper.update(values, DatabaseHelper._ID + "=" + id, null);
                } else {
                    rowsUpdated = myHelper.update(values, DatabaseHelper._ID + "=" + id + " and " + selection, selectionArgs);
                }

                break;

            default:
                // If the URI is not recognized, you should do some error handling here
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
