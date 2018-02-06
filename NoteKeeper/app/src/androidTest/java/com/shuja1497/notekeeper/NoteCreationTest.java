package com.shuja1497.notekeeper;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by shuja1497 on 2/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    // we need an activity .. since we are starting note creation process from notelistactivity so we will write that
    // now this activity will be created before each test and cleaned after each test
    @Rule
    ActivityTestRule<NoteListActivity> mActivityActivityRule =
            new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote(){

    }



}