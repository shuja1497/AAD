package com.shuja1497.notekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.shuja1497.notekeeper.NotekeeperDatabaseContract.CourseInfoEntry;
import com.shuja1497.notekeeper.NotekeeperDatabaseContract.NoteInfoEntry;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    //    public static final String NOTE_INFO = "com.shuja1497.notekeeper.NOTE_INFO";
    public static final String NOTE_ID = "com.shuja1497.notekeeper.NOTE_ID";
    public static final int ID_NOT_SET = -1;
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.shuja1497.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.shuja1497.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.shuja1497.notekeeper.ORIGINAL_NOTE_TEXT";
    private final String TAG = getClass().getSimpleName();
    private NoteInfo mNote = new NoteInfo(DataManager.getInstance().getCourses().get(0), "", "");
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int mNoteId;
    private boolean mIsCancelling;
    private ArrayAdapter<CourseInfo> adapter_courses;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;
    private NoteKeeperOpenHelper mDbOpenHelper;
    private Cursor mNoteCursor;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mNoteTextPos;
    private SimpleCursorAdapter mAdapterCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NoteKeeperOpenHelper(this);

        spinnerCourses = findViewById(R.id.spinner_courses);
//        List<CourseInfo> courses = DataManager.getInstance().getCourses();
//        adapter_courses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);

        // using cursor to populate the spinner .
        mAdapterCourses = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                null, new String[] {CourseInfoEntry.COLUMN_COURSE_TITLE},
                new int[] {android.R.id.text1}, 0);

        mAdapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(mAdapterCourses);

        loadCourseData();// not a good practise to load data in onCreate()

        readDisplayStateValues();// we get the note info out of the intent ..
        if (savedInstanceState == null)
            saveOriginalValues();// to preserve orignal values of the note
        else{
            restoreoriginalNoteValuesFromBundle(savedInstanceState);
        }
        textNoteTitle = findViewById(R.id.editText_note_title);
        textNoteText = findViewById(R.id.editText_note_text);

        // if its not a new note then display contents read from previous activity in this screen.
        if (!isNewNote)
        {
//            DisplayNotes();
            loadNoteData();
        }

        Log.d(TAG, "onCreate: ");
    }

    private void loadCourseData() {
        SQLiteDatabase db =  mDbOpenHelper.getReadableDatabase();

        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_ID,
                CourseInfoEntry._ID
                };

        Cursor cursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                null,null,null,null,
                CourseInfoEntry.COLUMN_COURSE_TITLE
                );

        mAdapterCourses.changeCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    private void loadNoteData() {

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        String courseId = "android_intents";
        String titleStart = "Dynamic";

//        // selection criteria
//        String selection = NoteInfoEntry.COLUMN_COURSE_ID+" = ? AND "
//                + NoteInfoEntry.COLUMN_NOTE_TITLE+" LIKE ?";
//
//        //selection values
//        String [] selectionArgs = {courseId, titleStart + "%"};

        String selection = NoteInfoEntry._ID+" = ?";
        String[] selectionArgs = {Integer.toString(mNoteId)};

        String[] noteColumns = {
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE_TEXT};

        mNoteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                selection, selectionArgs, null, null, null);

        mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);

        // right now cursor is before the first row.
        mNoteCursor.moveToNext();

        DisplayNotes();
    }

    private void restoreoriginalNoteValuesFromBundle(Bundle savedInstanceState) {
        originalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        originalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }

    private void saveOriginalValues() {

        if (isNewNote) // no values to save
            return;
        //saving the course Id
        originalNoteCourseId = mNote.getCourse().getCourseId();
        originalNoteTitle = mNote.getTitle();
        originalNoteText = mNote.getText();
        // we can use these if the user later cancels

    }

    @Override
    protected void onPause() {
            super.onPause();
            if (mIsCancelling){
                Log.i(TAG, "onPause: Cancelling note at position: "+ mNoteId);
                // if we are cancelling and we are in a process of creating new note
                if (isNewNote) {
                    // removing the note creating in backing store
                    DataManager.getInstance().removeNote(mNoteId);
                }else {
                    restorepreviousNoteValues();
                }
            }else {
                saveNote();
            }

        Log.d(TAG, "onPause: ");
    }

    private void restorepreviousNoteValues() {
        CourseInfo courseInfo = DataManager.getInstance().getCourse(originalNoteCourseId);
        mNote.setCourse(courseInfo);
        mNote.setTitle(originalNoteTitle);
        mNote.setText(originalNoteText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, originalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, originalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) spinnerCourses.getSelectedItem());
        mNote.setTitle(textNoteTitle.getText().toString());
        mNote.setText(textNoteText.getText().toString());
    }

    private void DisplayNotes() {

        // getting column values from the cursor

        String courseId = mNoteCursor.getString(mCourseIdPos);
        String noteTitle = mNoteCursor.getString(mNoteTitlePos);
        String noteText = mNoteCursor.getString(mNoteTextPos);
//
//        List<CourseInfo> courses = DataManager.getInstance().getCourses();
//        CourseInfo course = DataManager.getInstance().getCourse(courseId);

        // implementing the above code using cursor from our DB
//        int courseIndex = courses.indexOf(course);
        int courseIndex = getIndexOfCourseId(courseId);
        spinnerCourses.setSelection(courseIndex);
//        textNoteTitle.setText(mNote.getTitle());
//        textNoteText.setText(mNote.getText());

        // setting the value obtained from the cursor
        textNoteTitle.setText(noteTitle);
        textNoteText.setText(noteText);
    }

    private int getIndexOfCourseId(String courseId) {
        //getting reference to the cursoe that is used to populate the spinner
        Cursor cursor = mAdapterCourses.getCursor();
        //we can use this cursor to get the correct row
        int courseIdPos = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);//col index

        //we need the row index
        int courseRowIndex = 0;

        // now walk through the cursor row by row .
        // first bring the cursor to the start
        boolean more = cursor.moveToFirst();
        while(more){
            String cursorCourseId = cursor.getString(courseIdPos);
            if (courseId.equals(cursorCourseId)){
                break;
            }
            courseRowIndex++;
            more = cursor.moveToNext();
        }
        return courseRowIndex;
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
//        mNote = intent.getParcelableExtra(NOTE_INFO);
//        mNoteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        mNoteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        isNewNote = mNoteId == ID_NOT_SET;

        if (isNewNote) {
            createNewNote();
        }
        Log.i(TAG, "readDisplayStateValues: Note position :" + mNoteId);
//        mNote = DataManager.getInstance().getNotes().get(mNoteId);
    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNoteId = dm.createNewNote(); // got the psotion of the note
//        mNote = dm.getNotes().get(mNoteId);
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
            finish();// will exit this activity and go back to noteList activity but before that onPause will be called .
        }
        else if (id == R.id.action_next) {
            moveNext();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_next);
        int lastNodeIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNoteId <lastNodeIndex);
        return super.onPrepareOptionsMenu(menu);

    }

    private void moveNext() {
        saveNote();//to save any changes
        mNoteId++;
        mNote = DataManager.getInstance().getNotes().get(mNoteId);

        saveOriginalValues();//in case user cancels
        DisplayNotes();

        invalidateOptionsMenu();// system will automatically call onPrepareOption method
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
