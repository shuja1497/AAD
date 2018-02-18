package com.shuja1497.notekeeper;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shuja1497 on 2/8/18.
 */

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{

    private final Context mContext;
    private final List<CourseInfo> mcourses;
    private final LayoutInflater mLayoutInflater;

    public CourseRecyclerAdapter(Context context, List<CourseInfo> course) {
        mContext = context;
        mcourses = course;

        //in order to create views from layout resource we will use layoutInflator
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // for creating view holder instances
        // we need a context in order to create these views

        View itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // for associating data with our views
        CourseInfo courses = mcourses.get(position);
        holder.mTextCourse.setText(courses.getTitle());
        holder.mCurrentPosition = position;
    }

    @Override
    public int getItemCount() {
        // indicating no of data items we have
        return mcourses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;
        public int mCurrentPosition;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextCourse = itemView.findViewById(R.id.textView_course);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, mcourses.get(mCurrentPosition).getTitle(), Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
