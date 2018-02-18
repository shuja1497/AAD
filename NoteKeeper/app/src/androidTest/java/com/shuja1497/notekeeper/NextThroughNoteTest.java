package com.shuja1497.notekeeper;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by shuja1497 on 2/18/18.
 */
public class NextThroughNoteTest {

    // we want to check that whether we can select the note from this activity and then next the notes from the note activity.
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void NextThoroughNotes(){

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
    }

}