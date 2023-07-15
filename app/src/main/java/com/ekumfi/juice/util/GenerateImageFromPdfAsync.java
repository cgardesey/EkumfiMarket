package com.ekumfi.juice.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ekumfi.juice.constants.Const;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileOutputStream;

public class GenerateImageFromPdfAsync extends AsyncTask<String, Integer, Bitmap> {
    public static int drawSizeY;

    OnTaskPreExecuteInterface onTaskPreExecuteInterface;
    OnTaskCompletedInterface onTaskCompletedInterface;
    OnTaskProgressUpdateInterface onTaskProgressUpdateInterface;
    OnTaskCancelledInterface onTaskCancelledInterface;
    Activity activity;
    Uri pdfUri;
    int pageNumber;
    File file;

    public GenerateImageFromPdfAsync(OnTaskPreExecuteInterface onTaskPreExecuteInterface, OnTaskCompletedInterface onTaskCompletedInterface, OnTaskProgressUpdateInterface onTaskProgressUpdateInterface, OnTaskCancelledInterface onTaskCancelledInterface, Activity activity, Uri pdfUri, int pageNumber, File file) {
        this.onTaskPreExecuteInterface = onTaskPreExecuteInterface;
        this.onTaskCompletedInterface = onTaskCompletedInterface;
        this.onTaskProgressUpdateInterface = onTaskProgressUpdateInterface;
        this.onTaskCancelledInterface = onTaskCancelledInterface;
        this.activity = activity;
        this.pdfUri = pdfUri;
        this.pageNumber = pageNumber;
        this.file = file;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onTaskPreExecuteInterface.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... f_url) {
        PdfiumCore pdfiumCore = new PdfiumCore(activity);
        Bitmap bitmap = null;
        try {
            ParcelFileDescriptor fd = activity.getContentResolver().openFileDescriptor(pdfUri, "r");
            com.shockwave.pdfium.PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber - 1);


            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int Dwidth = displayMetrics.heightPixels;
            int Dheight = displayMetrics.widthPixels;

            double scaleBy = Math.min(Dwidth / (double)     pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber - 1), //
                    Dheight/ (double) pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber - 1));
            int width = (int) (pdfiumCore.getPageWidth( pdfDocument, pageNumber - 1));
            int height = (int) (pdfiumCore.getPageHeight(pdfDocument, pageNumber - 1));

            drawSizeY = height - (int) Const.convertDpToPx(activity, 200);
            //            drawSizeY = height;

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNumber - 1, 0, 0, width, height);

            // Start saving
            try {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                FileOutputStream outputStream = new FileOutputStream(file);

                // a bit long running
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                outputStream.close();
            } catch (Exception e) {
                Log.d("dfsaasd ", e.getMessage());
            }
            // End saving
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            //todo with exception
            Log.d("dfsaasd", e.toString());
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        onTaskCompletedInterface.onTaskCompleted(bitmap);
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

    public interface OnTaskPreExecuteInterface {
        void onPreExecute();
    }

    public interface OnTaskCompletedInterface {
        void onTaskCompleted(Bitmap bitmap);
    }

    public interface OnTaskProgressUpdateInterface {
        void onTaskProgressUpdate(int progress);
    }

    public interface OnTaskCancelledInterface {
        void onTaskCancelled();
    }
}
