package com.shuja1497.notekeeper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by shuja1497 on 2/6/18.
 */
public class DataManagerTest {
    @Test
    public void createNewNote() throws Exception {
        // for creating new note ..requirements --> course, note title, note text

        DataManager dm = DataManager.getInstance();
        final CourseInfo courseInfo = dm.getCourse("android_async");
        final String noteTitle  = "Test note title";
        final String noteText = "Test note text";

        int noteIndex = dm.createNewNote();
        NoteInfo newNote = dm.getNotes().get(noteIndex);

        newNote.setCourse(courseInfo);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNote = dm.getNotes().get(noteIndex);

        //Assert class ia static import so we can just directly use the mehtods of that class.
//        assertSame(newNote, compareNote); //checks if two references point to same objects
        // to know that the value we expect to be there are actually there.
        assertEquals(compareNote.getCourse(), courseInfo);
        assertEquals(compareNote.getTitle(), noteTitle);
        assertEquals(compareNote.getText(), noteText);
    }

}