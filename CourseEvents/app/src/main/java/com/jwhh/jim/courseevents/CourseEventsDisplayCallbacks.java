package com.jwhh.jim.courseevents;

/**
 * Created by Jim.
 */
// this is the interface broadcast receiver will use to pass info about the received broadcast
interface CourseEventsDisplayCallbacks {
    void onEventReceived(String courseId, String courseMessage);
}
