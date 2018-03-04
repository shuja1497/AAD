package com.shuja1497.notekeeper;

import android.annotation.SuppressLint;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
// we need to have a permission for this to act as a job service class ... in manifest
public class NoteUploaderJobService extends JobService {

    public static final String EXTRA_DATA_URI = "com.shuja1497.notekeeper.extra.DATA_URI";
    private NoteUploader mNoteUploader;

    public NoteUploaderJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {

        @SuppressLint("StaticFieldLeak")
        AsyncTask<JobParameters, Void, Void> task = new AsyncTask<JobParameters, Void, Void>() {
            @Override
            protected Void doInBackground(JobParameters... backgroundParameters) {

                JobParameters jobParameters = backgroundParameters[0];
                String stringDataUri = jobParameters.getExtras().getString(EXTRA_DATA_URI);
                Uri dataUri = Uri.parse(stringDataUri);
                mNoteUploader.doUpload(dataUri);

                jobFinished(jobParameters, false);

                return null;
            }
        };

        mNoteUploader = new NoteUploader(this);

        task.execute(params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
