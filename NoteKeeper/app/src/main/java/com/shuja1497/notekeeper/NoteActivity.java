package com.shuja1497.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    //    public static final String NOTE_INFO = "com.shuja1497.notekeeper.NOTE_INFO";
    public static final String NOTE_POSITION = "com.shuja1497.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int notePosition;
    private boolean mIsCancelling;
    private ArrayAdapter<CourseInfo> adapter_courses;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        adapter_courses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapter_courses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapter_courses);

        readDisplayStateValues();// we get the note info out of the intent ..
        saveOriginalValues();// to preserve orignal values of the note

        textNoteTitle = findViewById(R.id.editText_note_title);
        textNoteText = findViewById(R.id.editText_note_text);

        // if its not a new note then display contents read from previous activity in this screen.
        if (isNewNote)
        {
            createNewNote();
        }
        else {
            DisplayNotes(spinnerCourses, textNoteTitle, textNoteText);
        }

    }

    private void saveOriginalValues() {

        if (isNewNote) // no values to set
            return;
        else {
            //saving the course Id
            originalNoteCourseId = mNote.getCourse().getCourseId();
            originalNoteTitle = mNote.getTitle();
            originalNoteText = mNote.getText();
            // we can use these if the user later cancels.
        }

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote(); // got the psotion of the note
        mNote = dm.getNotes().get(notePosition);
    }

    @Override
    protected void onPause() {
            super.onPause();
            if (mIsCancelling){
                // if we are cancelling and we are in a process of creating new note
                if (isNewNote) {
                    // removing the note creating in backing store
                    DataManager.getInstance().removeNote(notePosition);
                }else {
                    restorepreviousNoteValues();
                }
            }else {
                saveNote();
            }
    }

    private void restorepreviousNoteValues() {
        CourseInfo courseInfo = DataManager.getInstance().getCourse(originalNoteCourseId);
        mNote.setCourse(courseInfo);
        mNote.setTitle(originalNoteTitle);
        mNote.setText(originalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        mNote.setTitle(textNoteTitle.getText().toString());
        mNote.setText(textNoteText.getText().toString());
    }

    private void DisplayNotes(Spinner spinner_courses, EditText textNoteTitle, EditText textNoteText) {

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseId = courses.indexOf(mNote.getCourse());

        spinner_courses.setSelection(courseId);
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
//        mNote = intent.getParcelableExtra(NOTE_INFO);
        int position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        isNewNote = position == POSITION_NOT_SET;

        if (!isNewNote) {
            mNote = DataManager.getInstance().getNotes().get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendMail();
            return true;
        }

        else if (id == R.id.action_camera) {
            startActivity(new Intent(this, CameraActivity.class));
            return true;
        }
        else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();// will exit this axit this activity and go back to noteList activity but before that onPause will be called .
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendMail() {

        CourseInfo courseInfo = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject  = textNoteTitle.getText().toString();
        String text  = "Checkout what i learned in the course \n" +
                courseInfo.getTitle() + "\n" + textNoteText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        startActivity(intent);
    }
}
