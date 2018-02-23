package com.shuja1497.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter mNoteRecyclerAdapter;

//    private ArrayAdapter<NoteInfo> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });
        initializeNoteList();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        adapter.notifyDataSetChanged();
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeNoteList() {

//        final ListView notesList = findViewById(R.id.list_notes);
//        ArrayList<NoteInfo> noteInfos = (ArrayList<NoteInfo>) DataManager.getInstance().getNotes();
//        adapter = new ArrayAdapter<NoteInfo>(this, android.R.layout.simple_list_item_1, noteInfos);
//        notesList.setAdapter(adapter);
//
//        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
////                NoteInfo notes = (NoteInfo) notesList.getItemAtPosition(position);
////                intent.putExtra(NoteActivity.NOTE_INFO, notes);
//                intent.putExtra(NoteActivity.NOTE_ID, position); // no need od parceable
//                startActivity(intent);
//            }
//        });

        final RecyclerView recyclerNotes = findViewById(R.id.list_notes);
        final LinearLayoutManager notesLinearlayoutManager = new LinearLayoutManager(this);

        recyclerNotes.setLayoutManager(notesLinearlayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);

        recyclerNotes.setAdapter(mNoteRecyclerAdapter);
        //updating noterecyler adpater when somehting is changed

    }
}
