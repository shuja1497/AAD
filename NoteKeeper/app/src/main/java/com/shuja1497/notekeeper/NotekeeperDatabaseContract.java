package com.shuja1497.notekeeper;

import android.provider.BaseColumns;

/**
 * Created by shuja1497 on 2/21/18.
 */

//SQLite does not have rigid typing . Any column can store any values

public final class NotekeeperDatabaseContract {

    public NotekeeperDatabaseContract() {
    }

    public static final class CourseInfoEntry implements BaseColumns{

        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_COURSE_TITLE = "course_title";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE "+ TABLE_NAME + " (" +
                        _ID + "INTEGER PRIMARY KEY, " +
                        COLUMN_COURSE_ID + "TEXT UNIQUE NOT NULL , " +
                        COLUMN_COURSE_TITLE + "TEXT NOT NULL )";
    }

    public static final class NoteInfoEntry implements BaseColumns{

        public static final String TABLE_NAME = "note_info";
        public static final String COLUMN_NOTE_TEXT = "note_text";
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_COURSE_ID = "course_id";


        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE "+ TABLE_NAME + " (" +
                        _ID + "INTEGER PRIMARY KEY, " +
                        COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                        COLUMN_NOTE_TEXT + " TEXT , " +
                        COLUMN_COURSE_ID + "TEXT NOT NULL)";
    }
}
