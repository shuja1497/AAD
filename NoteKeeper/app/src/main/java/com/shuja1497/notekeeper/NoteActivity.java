package com.shuja1497.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_INFO = "com.shuja1497.notekeeper.NOTE_INFO";
    private NoteInfo noteInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner spinner_courses = findViewById(R.id.spinner_courses);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapter_courses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapter_courses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_courses.setAdapter(adapter_courses);

        readDisplayStateValues();

        EditText textNoteTitle = findViewById(R.id.editText_note_title);
        EditText textNoteText = findViewById(R.id.editText_note_text);

        DisplayNotes(spinner_courses, textNoteTitle, textNoteText);

    }

    private void DisplayNotes(Spinner spinner_courses, EditText textNoteTitle, EditText textNoteText) {

        List <CourseInfo> courses = DataManager.getInstance().getCourses();
        int courseId = courses.indexOf(noteInfo.getCourse());

        spinner_courses.setSelection(courseId);
        textNoteTitle.setText(noteInfo.getTitle());
        textNoteText.setText(noteInfo.getText());

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        noteInfo = intent.getParcelableExtra(NOTE_INFO);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
