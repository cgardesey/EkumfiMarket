package com.ekumfi.juice.util;

import static com.ekumfi.juice.constants.Const.isExternalStorageWritable;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.ekumfi.juice.activity.MessageActivity;
import com.ekumfi.juice.constants.Const;
import com.yalantis.ucrop.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFileAsync extends AsyncTask<String, Integer, String> {

    Activity activity;
    OnTaskCompletedInterface onTaskCompletedInterface;
    OnTaskProgressUpdateInterface onTaskProgressUpdateInterface;
    OnTaskCancelledInterface onTaskCancelledInterface;

    public DownloadFileAsync(Activity activity, OnTaskCompletedInterface onTaskCompletedInterface, OnTaskProgressUpdateInterface onTaskProgressUpdateInterface, OnTaskCancelledInterface onTaskCancelledInterface) {
        this.activity = activity;
        this.onTaskCompletedInterface = onTaskCompletedInterface;
        this.onTaskProgressUpdateInterface = onTaskProgressUpdateInterface;
        this.onTaskCancelledInterface = onTaskCancelledInterface;
    }

    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, URLUtil.guessFileName(f_url[1], null, null));
            String mimeType = Const.getMimeType(f_url[1]);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            String relative_path = f_url[2];
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relative_path);


            switch (f_url[3]) {
                case "image":
                    activity.getContentResolver().delete(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.MediaColumns.DISPLAY_NAME + "='" + URLUtil.guessFileName(f_url[1], null, null) + "'", null
                    );
                    break;
                case "video":
                    activity.getContentResolver().delete(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.MediaColumns.DISPLAY_NAME + "='" + URLUtil.guessFileName(f_url[1], null, null) + "'", null
                    );
                    break;
                case "audio":
                    activity.getContentResolver().delete(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            MediaStore.MediaColumns.DISPLAY_NAME + "='" + URLUtil.guessFileName(f_url[1], null, null) + "'", null
                    );
                    break;
            }



            Uri externalContentUri = null;
            if (mimeType.contains("image")) {
                externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            else if (mimeType.contains("video")) {
                externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            }
            else if (mimeType.contains("audio")) {
                externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            Uri localImageUri = activity.getContentResolver().insert(externalContentUri, contentValues);
            if (localImageUri == null) {
                Log.d("asdffds", "Failed to create new MediaStore record");
            } else {
                Log.d("asdffds", "Created new MediaStore record");
                try {
                    OutputStream output = activity.getContentResolver().openOutputStream(localImageUri);

                    byte[] data = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress((int) ((total * 100) / lenghtOfFile));

                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("asdffds", "Failed to store bitmap");
                }
            }














        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return e.toString();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        onTaskCompletedInterface.onTaskCompleted(response);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        onTaskProgressUpdateInterface.onTaskProgressUpdate(values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        onTaskCancelledInterface.onTaskCancelled();
    }

    public interface OnTaskCompletedInterface {
        void onTaskCompleted(String file_url);
    }

    public interface OnTaskProgressUpdateInterface {
        void onTaskProgressUpdate(int progress);
    }

    public interface OnTaskCancelledInterface {
        void onTaskCancelled();
    }
}
