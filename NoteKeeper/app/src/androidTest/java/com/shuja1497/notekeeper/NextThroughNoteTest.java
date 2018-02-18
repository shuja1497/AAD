package com.shuja1497.notekeeper;

import android.support.design.widget.NavigationView;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

        // checking the drawer layout
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // checking the navigationdrawerview and selecting the notes option
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));

        //selecting item in recycler view
        onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        List<NoteInfo> notes  = DataManager.getInstance().getNotes();
        int index = 0;

        NoteInfo note = notes.get(index);

        // checking whether the title and text present on the screen is correct

        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(note.getCourse().getTitle())));

        onView(withId(R.id.editText_note_title)).check(matches(withText(note.getTitle())));

        onView(withId(R.id.editText_note_text)).check(matches(withText(note.getText())));

    }

}