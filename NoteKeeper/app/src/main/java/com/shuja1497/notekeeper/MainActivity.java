package com.shuja1497.notekeeper;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.shuja1497.notekeeper.NotekeeperDatabaseContract.CourseInfoEntry;
import com.shuja1497.notekeeper.NotekeeperDatabaseContract.NoteInfoEntry;

import java.util.List;

import static com.shuja1497.notekeeper.NoteActivity.LOADER_NOTES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private RecyclerView mRecyclerItems;
    private LinearLayoutManager mNotesLinearlayoutManager;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private GridLayoutManager mCoursesLayoutManager;

    private NoteKeeperOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NoteKeeperOpenHelper(this);// opened in onCreate . close in onDestroy

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));

            }
        });

        // for setting default values
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_data_sync, false);
        PreferenceManager.setDefaultValues(this, R.xml.pref_notification, false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);// now our activity will be notified if a user makes a selection from the navdrawer

        initializeNoteList();
    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // gets called each time we return to an activity
        super.onResume();
//        adapter.notifyDataSetChanged();
//        mNoteRecyclerAdapter.notifyDataSetChanged(); // notifying adapter with any possible changes whenever we return to this activity .

        // get the latest data out of the database
//        loadNotesFromDatabase();
// initLoader only checks that whehtehr the loader is instrantiated or not . so after the first it
// will directly go to onLoaderFinsished and won't requery. so we should use restartLoader instead of initLoader
        getLoaderManager().restartLoader(LOADER_NOTES, null, this);//to always re-query
        updateNavigationView();
    }

    private void loadNotesFromDatabase() {
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();// making DB connection

        final String[] noteColumns = {
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE_TEXT,
                NoteInfoEntry._ID};
        String notesOrderBy = NoteInfoEntry.COLUMN_COURSE_ID+", "+ NoteInfoEntry.COLUMN_NOTE_TITLE+" DESC";

        final Cursor noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                null, null, null, null, notesOrderBy);

        mNoteRecyclerAdapter.changeCursor(noteCursor);// associating cursor with the adapter

    }

    private void updateNavigationView() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textUserName = headerView.findViewById(R.id.text_user_name);
        TextView textUserEmail = headerView.findViewById(R.id.text_user_email);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name  = sharedPreferences.getString("user_display_name", "");
        String email_address  = sharedPreferences.getString("user_email_address", "");

        textUserName.setText(name);
        textUserEmail.setText(email_address);

    }

    private void initializeNoteList() {

        DataManager.loadFromdatabse(mDbOpenHelper);

        mRecyclerItems = findViewById(R.id.list_items);
        mNotesLinearlayoutManager = new LinearLayoutManager(this);

        mCoursesLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.course_grid_span));

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this,null);// no cursor right now so null

        List<CourseInfo> courses  = DataManager.getInstance().getCourses();
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);

        displayNotes();
    }

    private void displayNotes() {
        mRecyclerItems.setLayoutManager(mNotesLinearlayoutManager);
        mRecyclerItems.setAdapter(mNoteRecyclerAdapter);
        //updating noterecyler adpater when something is changed

        // connecting to the db and allows us to interact with the database
//        SQLiteDatabase mreadableDatabase = mDbOpenHelper.getReadableDatabase();

        // we need to check the menu item in navView .
        selectNavMenuItem(R.id.nav_notes);
    }

    private void selectNavMenuItem(int id) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    private void displayCourses(){
        mRecyclerItems.setLayoutManager(mCoursesLayoutManager);
        mRecyclerItems.setAdapter(mCourseRecyclerAdapter);

        selectNavMenuItem(R.id.nav_courses);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
//            handleSelection("Notes");
            displayNotes();
        } else if (id == R.id.nav_courses) {
//            handleSelection("Courses");
            displayCourses();
        }else if (id == R.id.nav_share) {
//            handleSelection(R.string.nav_share_message);
            handleShare();

        } else if (id == R.id.nav_send) {
            handleSelection(R.string.nav_send_message);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleShare() {
        View view  = findViewById(R.id.list_items);
        Snackbar.make(view, "Share to - "+
                PreferenceManager.getDefaultSharedPreferences(this).getString("user_favourite_social", "")
                ,Snackbar.LENGTH_LONG).show();


    }

    private void handleSelection(int message_id) {
        //using snackbar
        // we want a view from the current activity

        View view  = findViewById(R.id.list_items);
        // snackbar can directly use string resources without taking the actual string value
        Snackbar.make(view, message_id,Snackbar.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = null;

//        if (id==LOADER_NOTES) {
//            loader =  new CursorLoader(this) {
//                @Override
//                public Cursor loadInBackground() {
//                    SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();// making DB connection
//
////                    final String[] noteColumns = {
////                            NoteInfoEntry.COLUMN_COURSE_ID,
////                            NoteInfoEntry.COLUMN_NOTE_TITLE,
////                            NoteInfoEntry._ID};
////
//                    final String[] noteColumns = {
//                            NoteInfoEntry.COLUMN_NOTE_TITLE,
//                            NoteInfoEntry.getQName(NoteInfoEntry._ID),
//                            CourseInfoEntry.COLUMN_COURSE_TITLE};
//
//                    final String notesOrderBy = CourseInfoEntry.COLUMN_COURSE_TITLE + ", " +
//                            NoteInfoEntry.COLUMN_NOTE_TITLE ;
//
//                    String tablesWithJoin = NoteInfoEntry.TABLE_NAME+" JOIN "+
//                            CourseInfoEntry.TABLE_NAME +" ON "+
//                            NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID) +"="+
//                            CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);
//
//                    return db.query(tablesWithJoin, noteColumns,
//                            null, null, null, null, notesOrderBy);
//                }
//            };
//        }
//        return loader;

        // using content provider
        if(id == LOADER_NOTES){
            final String[] noteColumns = {
                            NoteKeeperProviderContract.Notes.COLUMN_NOTE_TITLE,
//                            NoteInfoEntry.getQName(NoteInfoEntry._ID),
                    // no need of getting qualified columns . Content Provider will handkle on its own
                            NoteKeeperProviderContract.Notes._ID,
                            NoteKeeperProviderContract.Notes.COLUMN_COURSE_TITLE};

            // using Content Providers Note class instead of directly using database
            final String notesOrderBy = NoteKeeperProviderContract.Notes.COLUMN_COURSE_TITLE + ", " +
                            NoteKeeperProviderContract.Notes.COLUMN_NOTE_TITLE ;

            loader = new CursorLoader(this, NoteKeeperProviderContract.Notes.CONTENT_EXPANDED_URI,
                    noteColumns, null, null , notesOrderBy);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId()==LOADER_NOTES)
            mNoteRecyclerAdapter.changeCursor(data);// associating cursor with the adapter

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId()==LOADER_NOTES)
            mNoteRecyclerAdapter.changeCursor(null);
    }
}
