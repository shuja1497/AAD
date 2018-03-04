package com.jwhh.jim.courseevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CourseEventsReceiver extends BroadcastReceiver {

    public static final String ACTION_COURSE_EVENT = "com.shuja1497.notekeeper.action.COURSE_EVENT";
    public static final String EXTRA_COURSE_ID =  "com.shuja1497.notekeeper.extra.COURSE_ID";
    public static final String EXTRA_COURSE_MESSAGE =  "com.shuja1497.notekeeper.extra.COURSE_MESSAGE";

    private CourseEventsDisplayCallbacks mCourseEventsDisplayCallbacks;


    public void setCourseEventsDisplayCallbacks(CourseEventsDisplayCallbacks courseEventsDisplayCallbacks) {
        mCourseEventsDisplayCallbacks = courseEventsDisplayCallbacks;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (ACTION_COURSE_EVENT.equals(intent.getAction())){
            String courseId = intent.getStringExtra(EXTRA_COURSE_ID);
            String courseMessage = intent.getStringExtra(EXTRA_COURSE_MESSAGE);
        }
        // now the receiver can extract the info from the intent .

        if (mCourseEventsDisplayCallbacks != null){
            mCourseEventsDisplayCallbacks.onEventReceived(courseId, courseMessage);// allows the user to call the interface method
            // anytime a broadcast is received .
        }
    }
}
