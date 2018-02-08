package com.shuja1497.notekeeper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shuja1497 on 2/8/18.
 */

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{

    private final Context mContext;
    private final List<NoteInfo> mNoteInfoList;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerAdapter(Context context, List<NoteInfo> noteInfoList) {
        mContext = context;
        mNoteInfoList = noteInfoList;

        //in order to create views from layout resource we will use layoutInflator
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // for creating view holder instances
        // we need a context in order to create these views

        View itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // for associating data with our views
        NoteInfo noteInfo = mNoteInfoList.get(position);
        holder.mTextCourse.setText(noteInfo.getCourse().getTitle());
        holder.mTextTitle.setText(noteInfo.getTitle());
    }

    @Override
    public int getItemCount() {
        // indicating no of data items we have
        return mNoteInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;
        public final TextView mTextTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextCourse = itemView.findViewById(R.id.textView_course);
            mTextTitle = itemView.findViewById(R.id.textView_note);
        }
    }
}
