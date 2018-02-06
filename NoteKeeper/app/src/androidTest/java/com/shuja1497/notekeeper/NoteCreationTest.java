package com.shuja1497.notekeeper;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

// made static imports of these classes so that we can use their static methods here as just a nomrla method .
// to make test more readable
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

/**
 * Created by shuja1497 on 2/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    // we need an activity .. since we are starting note creation process from notelistactivity so we will write that
    // now this activity will be created before each test and cleaned after each test
    // this will be the test environment.. will take care of launching the activity
    @Rule
    public ActivityTestRule<NoteListActivity> mActivityActivityRule =
            new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote(){
        // first interaction is with fab .. so get a reference to that using onView() and a matcher withId(R.id.fab)
//        ViewInteraction FabNewNote = onView(withId(R.id.fab));
        // now we can interact with this fab button

        // specifying particular actions we will perform.
//        FabNewNote.perform(click());

        onView(withId(R.id.fab)).perform(click());

        // next we will type the title of tha note.
        // but we will come inside the NoteActivity after clicking fab button .

        onView(withId(R.id.editText_note_title)).perform(typeText("Test note title"));
        onView(withId(R.id.editText_note_text)).perform(typeText("this is the body of my text note"),
                closeSoftKeyboard());

    }
}