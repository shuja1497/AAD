package com.shuja1497.notekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shuja1497.notekeeper.NotekeeperDatabaseContract.CourseInfoEntry;
import com.shuja1497.notekeeper.NotekeeperDatabaseContract.NoteInfoEntry;

/**
 * Created by shuja1497 on 2/21/18.
 */

// we get connection to the database using this class

public class NoteKeeperOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notekeeper.db";
//    public static final int DATABASE_VERSION = 1;
    // after adding the index we are versioning the DB
    public static final int DATABASE_VERSION = 2;// contains tables and indexes

    public NoteKeeperOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CourseInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(NoteInfoEntry.SQL_CREATE_TABLE);

        // creating the indexes
        db.execSQL(CourseInfoEntry.SQL_CREATE_INDEX1);
        db.execSQL(NoteInfoEntry.SQL_CREATE_INDEX1);
        DatabaseDataWorker worker = new DatabaseDataWorker(db);

        worker.insertCourses();
        worker.insertSampleNotes();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion<2){
            db.execSQL(CourseInfoEntry.SQL_CREATE_INDEX1);
            db.execSQL(NoteInfoEntry.SQL_CREATE_INDEX1);
        }
    }
}
