package com.ekumfi.juice.activity;

import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.R;
import com.ekumfi.juice.receiver.NetworkReceiver;

public class OfficailWebsiteActivity extends AppCompatActivity {

    NetworkReceiver networkReceiver;
    WebView descriptionview;
    ImageView loadinggif;
    String url;

    Button btn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_website);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        descriptionview = findViewById(R.id.descriptionview);
        loadinggif = findViewById(R.id.loadinggif);

        Glide.with(getApplicationContext()).asGif().load(R.drawable.spinner).apply(new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.spinner)
                .error(R.drawable.error)).into(loadinggif);

        descriptionview.getSettings().setJavaScriptEnabled(true);
        descriptionview.loadUrl(url);
        descriptionview.getSettings().setDomStorageEnabled(true);
        descriptionview.getSettings().setAppCacheEnabled(true);
        descriptionview.getSettings().setLoadsImagesAutomatically(true);
        descriptionview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        descriptionview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                descriptionview.setVisibility(View.VISIBLE);
                view.scrollTo(0, 0);
                loadinggif.setVisibility(View.GONE);
            }
        });

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
}
