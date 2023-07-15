//      بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ

package com.ekumfi.juice.activity;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.greysonparrelli.permiso.PermisoActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ekumfi.juice.R;

import afriwan.ahda.AudioStreaming;

public class RadioActivity extends PermisoActivity {

    private AudioStreaming audioStreamingCustomFont;
    String stream_url = "http://live-hls-web-aje.getaj.net/AJE/06.m3u8";

    NetworkReceiver networkReceiver;
    TextView stationname, frequency;
    RoundedImageView icon;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        audioStreamingCustomFont = findViewById(R.id.playCustomFonts);
        stationname = findViewById(R.id.stationname);
        frequency = findViewById(R.id.frequency);
        icon = findViewById(R.id.icon);
        Typeface iconFont = Typeface.createFromAsset(getAssets(), "audio-player-view-font-custom.ttf");
        audioStreamingCustomFont.setTypeface(iconFont);
        audioStreamingCustomFont.withUrl(getIntent().getStringExtra("STREAM_URL"));


        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("ICON_URL")).apply(new RequestOptions()
                .centerCrop()
                .placeholder(null)
                .error(R.drawable.error))
                .into(icon);

        stationname.setText(getIntent().getStringExtra("STATION_NAME"));
        frequency.setText(getIntent().getStringExtra("FREQUENCY"));

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
    protected void onDestroy() {
        audioStreamingCustomFont.destroy();
        super.onDestroy();
    }
}
