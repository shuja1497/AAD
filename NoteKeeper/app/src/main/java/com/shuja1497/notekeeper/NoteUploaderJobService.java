package com.shuja1497.notekeeper;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
// we need to have a permission for this to act as a job service class ... in manifest
public class NoteUploaderJobService extends JobService {

    public static final String EXTRA_DATA_URI = "com.shuja1497.notekeeper.extra.DATA_URI";

    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
