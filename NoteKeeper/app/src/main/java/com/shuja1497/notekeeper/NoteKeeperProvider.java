package com.shuja1497.notekeeper;

// Steps new > other > Content Provider
// authority : package name followed by .provider

// content provider must be implemented over the top of SQLite

import android.content.ContentProvider;
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

    private NoteKeeperOpenHelper mDbOpenHelper;

    // adding UriMatcher field
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSE = 0;

    public static final int NOTES = 1;

    public static final int NOTES_EXPANDED = 2;

    // adding list of valid Uris using static initializer
    static {
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Courses.PATH, COURSE);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH, NOTES);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH_EXPANDED, NOTES_EXPANDED);

        // after this we can handle Uris in query method
    }

    public NoteKeeperProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
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
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
