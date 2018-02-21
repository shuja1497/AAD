package com.shuja1497.notekeeper;

/**
 * Created by shuja1497 on 2/21/18.
 */

public final class NotekeeperDatabaseContract {

    public NotekeeperDatabaseContract() {
    }

    public static final class CourseInfoEntry{

        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_COURSE_TITLE = "course_title";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE "+ TABLE_NAME + " (" +
                        COLUMN_COURSE_ID + " , " +
                        COLUMN_COURSE_TITLE + ")";
    }

    public static final class NoteInfoEntry{

        public static final String TABLE_NAME = "note_info";
        public static final String COLUMN_NOTE_TEXT = "note_text";
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_COURSE_ID = "course_id";


        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE "+ TABLE_NAME + " (" +
                        COLUMN_NOTE_TITLE + " , " +
                        COLUMN_NOTE_TEXT + " , " +
                        COLUMN_COURSE_ID + ")";
    }
}
