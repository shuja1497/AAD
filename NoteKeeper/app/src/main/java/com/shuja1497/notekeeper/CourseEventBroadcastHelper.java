package com.shuja1497.notekeeper;

import android.content.Context;
import android.content.Intent;

public class CourseEventBroadcastHelper {

    public static final String ACTION_COURSE_EVENT = "com.shuja1497.notekeeper.action.COURSE_EVENT";
    public static final String EXTRA_COURSE_ID =  "com.shuja1497.notekeeper.extra.COURSE_ID";
    public static final String EXTRA_COURSE_MESSAGE =  "com.shuja1497.notekeeper.extra.COURSE_MESSAGE";

    public static void sendEventBroadcast(Context context, String courseId, String message) {

        Intent intent = new Intent(ACTION_COURSE_EVENT); // adding action
        intent.putExtra(EXTRA_COURSE_ID, courseId); // extras
        intent.putExtra(EXTRA_COURSE_MESSAGE, message); // extras

        context.sendBroadcast(intent);
    }
}
