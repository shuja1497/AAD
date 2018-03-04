package com.jwhh.jim.courseevents;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

// display the info received from the broadcast
public class CourseEventsMainActivity extends AppCompatActivity
        implements CourseEventsDisplayCallbacks {

    ArrayList<String> mCourseEvents;
    ArrayAdapter<String> mCourseEventsAdapter;
    private CourseEventsReceiver mCourseEventsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_events_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupListView();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    protected void setupListView() {
        mCourseEvents = new ArrayList<String>();
        mCourseEventsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCourseEvents);

        final ListView listView = (ListView) findViewById(R.id.list_course_events);
        listView.setAdapter(mCourseEventsAdapter);

        setupCourseEventReceiver();
    }

    private void setupCourseEventReceiver() {
        mCourseEventsReceiver = new CourseEventsReceiver();
        mCourseEventsReceiver.setCourseEventsDisplayCallbacks(this);
    }

    @Override
    public void onEventReceived(String courseId, String courseMessage) {

        String displayText = courseId + " : " + courseMessage ;
        mCourseEvents.add(displayText);
        mCourseEventsAdapter.notifyDataSetChanged();
    }
}
