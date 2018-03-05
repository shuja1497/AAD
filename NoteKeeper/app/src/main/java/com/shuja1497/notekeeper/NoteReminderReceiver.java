package com.shuja1497.notekeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoteReminderReceiver extends BroadcastReceiver {

    // in order to display notification we will need extras

    public static final String EXTRA_NOTE_TITLE = "com.shuja1497.notekeeper.extra.NOTE_TITLE";
    public static final String EXTRA_NOTE_TEXT = "com.shuja1497.notekeeper.extra.NOTE_TEXT";
    public static final String EXTRA_NOTE_ID = "com.shuja1497.notekeeper.extra.NOTE_ID";


    @Override
    public void onReceive(Context context, Intent intent) {
        String noteTitle = intent.getStringExtra(EXTRA_NOTE_TITLE);
        String noteText = intent.getStringExtra(EXTRA_NOTE_TEXT);
        int noteID = intent.getIntExtra(EXTRA_NOTE_ID, 0);

        NotesReminderNotification.notify(context, noteTitle, noteText, noteID);

        // now we have a broadcast receiver that display our note reminder notification
    }
}
