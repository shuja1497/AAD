package com.shuja1497.notekeeper;

import android.net.Uri;

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

    // creating nested classes for each of the table that we expose with this content provider

    // courses Table
    public static final class Courses {
        public static final String PATH = "courses";
        //"content://com.shuja1497.notekeeper.provider/courses"
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }

    // notes Table
    public static final class Notes{
        public static final String PATH = "notes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }
}
