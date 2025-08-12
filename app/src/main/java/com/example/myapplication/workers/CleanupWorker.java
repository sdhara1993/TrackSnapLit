package com.example.tracksnaplite.workers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;

public class CleanupWorker extends Worker {

    private static final String TAG = "CleanupWorker";

    public CleanupWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "Starting cleanup task...");

        // Define the file to delete
        File kmlFile = new File(
                getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "tracklog.kml"
        );

        // Attempt deletion
        boolean deleted = safeDelete(kmlFile);

        if (deleted) {
            Log.i(TAG, "Cleanup successful: " + kmlFile.getAbsolutePath());
            return Result.success();
        } else {
            Log.w(TAG, "Cleanup failed: " + kmlFile.getAbsolutePath());
            return Result.failure();
        }
    }

    private boolean safeDelete(File file) {
        if (file != null && file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                Log.w(TAG, "Failed to delete file: " + file.getAbsolutePath());
            }
            return deleted;
        } else {
            Log.w(TAG, "File not found or null: " + (file != null ? file.getAbsolutePath() : "null"));
            return false;
        }
    }
}
