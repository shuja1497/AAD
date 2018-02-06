package com.shuja1497.notekeeper;

import android.provider.ContactsContract;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by shuja1497 on 2/6/18.
 */
public class DataManagerTest {

    static DataManager sDataManager;

    @BeforeClass
    public static void ClassSetup() throws Exception{
        sDataManager = DataManager.getInstance();
    }

    @Before
    public void setUp() throws Exception{
        sDataManager.getNotes().clear();// each time the test runs i have a note list which is completely empty
        sDataManager.initializeExampleNotes();// each test now starts with a consistent set of notes each time.
    }
    @Test
    public void createNewNote() throws Exception {
        // for creating new note ..requirements --> course, note title, note text

        final CourseInfo courseInfo = sDataManager.getCourse("android_async");
        final String noteTitle  = "Test note title";
        final String noteText = "Test note text";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);

        newNote.setCourse(courseInfo);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);

        //Assert class ia static import so we can just directly use the mehtods of that class.
//        assertSame(newNote, compareNote); //checks if two references point to same objects
        // to know that the value we expect to be there are actually there.
        assertEquals(compareNote.getCourse(), courseInfo);
        assertEquals(compareNote.getTitle(), noteTitle);
        assertEquals(compareNote.getText(), noteText);
    }

    //to verify that the findNote() works correctly or not .
    @Test
    public void findSimilarNotes() throws Exception{


        final CourseInfo course  = sDataManager.getCourse("android_async");
        final String noteTitle = "Title for test2";
        final String noteText1 = "Text1 for test2";
        final String noteText2 = "Text2 for test2";

        int NoteIndex1 = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(NoteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        int NoteIndex2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(NoteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(NoteIndex1, foundIndex1);

        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(NoteIndex2, foundIndex2);



    }

}