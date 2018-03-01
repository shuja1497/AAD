package com.shuja1497.notekeeper;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by shuja1497 on 3/1/18.
 */

//CP are used to expose tables . they are only used to access the tables and may be different from
// the physical structure of underlying DB tables .
// this contract class is focuesed on the public appearance of the data
// key is base Uri .


public final class NoteKeeperProviderContract {

    // making private constructor so that no other can make instance of this class .
    private NoteKeeperProviderContract(){}
    public static final String AUTHORITY = "com.shuja1497.notekeeper.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);// used to create specific uri's for individual tables

    protected interface CoursesIdColumns{
        // to remove redundancy
        public static final String COLUMN_COURSE_ID = "course_id";
    }

    protected interface CoursesColumns{
        public static final String COLUMN_COURSE_TITLE = "course_title";
    }

    protected interface NotesColumns{
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";
    }

    // creating nested classes for each of the table that we expose with this content provider

    // courses Table
    public static final class Courses implements BaseColumns, CoursesColumns, CoursesIdColumns{
        public static final String PATH = "courses";
        //"content://com.shuja1497.notekeeper.provider/courses"
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }

    // notes Table
    // for extebded notes URI we need to extend the courseColumn interface also
    public static final class Notes implements BaseColumns, NotesColumns, CoursesIdColumns, CoursesColumns{
        public static final String PATH = "notes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);

        // we could have made a new table but it's actually an expanded version of notes table
        public static final String PATH_EXPANDED = "notes_expanded";
        public static final Uri CONTENT_EXPANDED_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_EXPANDED);
    }
}
