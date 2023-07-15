package com.ekumfi.juice.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

public class DownloadFilesAsync extends AsyncTask<String, Integer, String> {

    OnTaskCompletedInterface onTaskCompletedInterface;
    OnTaskProgressUpdateInterface onTaskProgressUpdateInterface;
    OnTaskCancelledInterface onTaskCancelledInterface;
    Map<String, String> filepathMap;

    public DownloadFilesAsync(OnTaskCompletedInterface onTaskCompletedInterface, OnTaskProgressUpdateInterface onTaskProgressUpdateInterface, OnTaskCancelledInterface onTaskCancelledInterface, Map<String, String> filepathMap) {
        this.onTaskCompletedInterface = onTaskCompletedInterface;
        this.onTaskProgressUpdateInterface = onTaskProgressUpdateInterface;
        this.onTaskCancelledInterface = onTaskCancelledInterface;
        this.filepathMap = filepathMap;
    }

    @Override
    protected String doInBackground(String... f_url) {
        int file_count = 0;
        Iterator it = filepathMap.entrySet().iterator();
        while (it.hasNext()) {
            file_count++;
            Map.Entry pair = (Map.Entry)it.next();

            int count;
            try {
                URL url = new URL((String) pair.getKey());
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                File destinationFile = new File((String) pair.getValue());
                if (!destinationFile.getParentFile().exists()) {
                    destinationFile.getParentFile().mkdirs();
                }
                // Output stream
                OutputStream output = new FileOutputStream((String) pair.getValue());

                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(new Integer[]{file_count, (int) ((total * 100) / lenghtOfFile)});

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                return e.toString();
            }

            it.remove(); // avoids a ConcurrentModificationException
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
        onTaskProgressUpdateInterface.onTaskProgressUpdate(values);
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
        void onTaskProgressUpdate(Integer[] progress);
    }

    public interface OnTaskCancelledInterface {
        void onTaskCancelled();
    }
}
