package com.example.courseregistrationwaitinglist;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StudentCourseProvider extends ContentProvider {

    /**
     * global unique string AUTHORITY identifies the content provider
     * one app have one authority
     */
    private static final String AUTHORITY = "com.example.courseregistrationwaitinglist.StudentCourseProvider";

    /**
     * passing table name in base path
     *
     */
    private static final String BASE_PATH = "student";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
    private static final int STUDENT = 1;
    private static final int STUDENT_ID = 2;
    /**
     * Urimatcher class passes Uri and tells which Uri operation requested
     * in this 2 operations are possible ,mentioned in static initializer
     */
    private static final android.content.UriMatcher UriMatcher = new UriMatcher(android.content.UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Student";

    static {
        UriMatcher.addURI(AUTHORITY, BASE_PATH, STUDENT);
        /**
         * /# IS NUMERICAL VALUE TO STUDENT GET DATA
         */
        UriMatcher. addURI(AUTHORITY, BASE_PATH + "/#", STUDENT_ID);

    }
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBopenHelper helper = new DBopenHelper(getContext());

        database = helper.getWritableDatabase();
        return true;
    }

    @androidx.annotation.Nullable
    @Override
    /*
     **retrieve one column or all col.
     */
    public Cursor query(@androidx.annotation.NonNull Uri uri,
                        @androidx.annotation.Nullable String[] projection,
                        @androidx.annotation.Nullable String selection,
                        @androidx.annotation.Nullable String[] selectionArgs,
                        @androidx.annotation.Nullable String sortOrder) {
/**
 * select & identify one row
 */
        if (UriMatcher.match(uri) == STUDENT_ID)
        {
            selection  = DBopenHelper.STUDENT_ID + "=" +uri.getLastPathSegment();
        }
        return database.query(
                DBopenHelper.TABLE_STUDENT,
                DBopenHelper.ALL_COLUMNS,
                selection,
                null,
                null,
                null,
                sortOrder); //DBopenHelper.STUDENT_CREATED + " DESC "



    }
    @androidx.annotation.Nullable
    @Override
    public String getType(@androidx.annotation.NonNull Uri uri) {
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    /**
     * return Uri which like line 41
     */

    public Uri insert(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values) {
        long id = database.insert(DBopenHelper.TABLE_STUDENT,null, values);
        return Uri.parse(BASE_PATH + "/" +id);
    }

    @Override
    public int delete(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        return database.delete(DBopenHelper.TABLE_STUDENT, selection, selectionArgs);
    }
    @Override
    public int update(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        return database.update(DBopenHelper.TABLE_STUDENT, values, selection, selectionArgs);
    }
}


