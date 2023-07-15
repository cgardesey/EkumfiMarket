package com.ekumfi.juice.activity;

import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ekumfi.juice.R;
import com.ekumfi.juice.receiver.NetworkReceiver;

import uk.co.senab.photoview.PhotoViewAttacher;


public class PictureActivity extends AppCompatActivity {

    NetworkReceiver networkReceiver;
    public static Bitmap idPicBitmap;

    private ImageView photoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photoImageView = findViewById(R.id.photoImageView);
        photoImageView.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
        photoImageView.getLayoutParams().height = getWindowManager().getDefaultDisplay().getHeight();
        photoImageView.setAdjustViewBounds(true);
        //photoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(photoImageView);

        photoImageView.setImageBitmap(idPicBitmap);

        networkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
