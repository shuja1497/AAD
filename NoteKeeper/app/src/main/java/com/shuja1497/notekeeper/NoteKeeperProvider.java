package com.shuja1497.notekeeper;

// Steps new > other > Content Provider
// authority : package name followed by .provider

// content provider must be implemented over the top of SQLite

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import com.shuja1497.notekeeper.NoteKeeperProviderContract.Courses;
import com.shuja1497.notekeeper.NoteKeeperProviderContract.CoursesColumns;
import com.shuja1497.notekeeper.NoteKeeperProviderContract.Notes;
import com.shuja1497.notekeeper.NotekeeperDatabaseContract.CourseInfoEntry;
import com.shuja1497.notekeeper.NotekeeperDatabaseContract.NoteInfoEntry;

public class NoteKeeperProvider extends ContentProvider {

    public static final String MIME_VENDOR_TYPE = "vnd." + NoteKeeperProviderContract.AUTHORITY + ".";
    private NoteKeeperOpenHelper mDbOpenHelper;

    // adding UriMatcher field
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSE = 0;

    public static final int NOTES = 1;

    public static final int NOTES_EXPANDED = 2;

    public static final int NOTES_ROW = 3;

    public static final int COURSES_ROW = 4;

    public static final int NOTES_EXPANDED_ROW = 5;

    // adding list of valid Uris using static initializer
    static {
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Courses.PATH, COURSE);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH, NOTES);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH_EXPANDED, NOTES_EXPANDED);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH+"/#", NOTES_ROW);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Courses.PATH + "/#", COURSES_ROW);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH_EXPANDED + "/#", NOTES_EXPANDED_ROW);
        // after this we can handle Uris in query method
    }

    public NoteKeeperProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case COURSE:
                nRows = db.delete(CourseInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES:
                nRows = db.delete(NoteInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(CourseInfoEntry.TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
            case NOTES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = NoteInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(NoteInfoEntry.TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
            case NOTES_EXPANDED_ROW:
                // throw exception saying that this is a read-only table
                break;
        }

        return nRows;
    }

    @Override
    public String getType(Uri uri) {
       // returning the mime type
        String mimeType = null;

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch){
            case COURSE:
                // returning more than one row therefore dir ...
                // if single row then use item instead od dir
                //vnd.android.cursor.dir/vnd.com.shuja1497.notekeeper.provider.courses
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Courses.PATH;
                break;

            case NOTES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH;
                break;

            case NOTES_EXPANDED:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;

            case NOTES_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH;
                break;

            case COURSES_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Courses.PATH;
                break;

            case NOTES_EXPANDED_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;
        }

        return mimeType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        long rowId = -1;
        // this method needs to return a uri of the newly identified row
        Uri rowUri = null;

        int uriMatch = sUriMatcher.match(uri);

        switch (uriMatch){
            case NOTES:
                rowId = db.insert(NoteInfoEntry.TABLE_NAME, null, values);
//                uri for the row >>> content://com.shuja1497.notekeeper.provider/rowId
                rowUri = ContentUris.withAppendedId(Notes.CONTENT_URI, rowId);
                break;

            case COURSE:
                rowId = db.insert(CourseInfoEntry.TABLE_NAME, null, values);
                rowUri = ContentUris.withAppendedId(Courses.CONTENT_URI, rowId);
                break;

            case NOTES_EXPANDED:
                // throw exception that this is only a read-only table
                break;
        }
        return rowUri;
    }

    @Override
    public boolean onCreate() {
        mDbOpenHelper = new NoteKeeperOpenHelper(getContext());
        return true;// return true to indicate that the content provide was created successfully
    }

    @Override
    // almost similar to SQLite
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        SQLiteDatabase db  = mDbOpenHelper.getReadableDatabase();
        String rowSelection = null;

        int uriMatch = sUriMatcher.match(uri);//will return appropraite integar value for the matched uri
        switch (uriMatch){
            case COURSE:
                cursor = db.query(CourseInfoEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case NOTES:
                cursor = db.query(NoteInfoEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTES_EXPANDED:
                cursor = notesExpandedQuery(db, projection, selection, selectionArgs, sortOrder);
                break;

            case NOTES_ROW:
                // we first need to specify the selection criteria to perform queries on specific rows
                // we won't use the selection parameters obtained rather specify explicitly
                long rowId = ContentUris.parseId(uri);
                rowSelection = NoteInfoEntry._ID + " = ?";
                String[] rowSelectionArgs = new String[]{Long.toString(rowId)};
                cursor = db.query(NoteInfoEntry.TABLE_NAME, projection,
                        rowSelection, rowSelectionArgs, null, null,
                        null);
                break;

            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                cursor = db.query(CourseInfoEntry.TABLE_NAME, projection, rowSelection,
                        rowSelectionArgs, null, null, null);
                break;

            case NOTES_EXPANDED_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = NoteInfoEntry.getQName(NoteInfoEntry._ID) + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                cursor = notesExpandedQuery(db, projection, rowSelection, rowSelectionArgs, null);
                break;
        }
        return cursor;
    }

    private Cursor notesExpandedQuery(SQLiteDatabase db, String[] projection,
                                      String selection, String[] selectionArgs, String sortOrder) {

        // we have to take care of the column qualidfied thing here

        String[] columns = new String[projection.length];
        for (int idx = 0; idx < projection.length ; idx++){

            // if the column in projection array is either _ID or COURSE_TITLE which are common in both
            // course and notes table then we need to qualify .
            if (projection[idx].equals(BaseColumns._ID) ||
                    projection[idx].equals(NoteKeeperProviderContract.CoursesIdColumns.COLUMN_COURSE_ID)){
                columns[idx] = NoteInfoEntry.getQName(projection[idx]);
            }
            else
                columns[idx] = projection[idx];
        }
        String tablesWithJoin  = NoteInfoEntry.TABLE_NAME + " JOIN "+
                CourseInfoEntry.TABLE_NAME + " ON " +
                NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID) + " = " +
                CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);
        return db.query(tablesWithJoin, columns, selection, selectionArgs,
                null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case COURSE:
                nRows = db.update(CourseInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES:
                nRows = db.update(NoteInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(CourseInfoEntry.TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
            case NOTES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = NoteInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(NoteInfoEntry.TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
            case NOTES_EXPANDED_ROW:
                // throw exception saying that this is a read-only table
                break;
        }
        return nRows;
    }
}
