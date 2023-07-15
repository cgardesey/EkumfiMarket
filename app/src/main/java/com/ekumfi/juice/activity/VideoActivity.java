package com.ekumfi.juice.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.otaliastudios.zoom.ZoomLogger;
import com.otaliastudios.zoom.ZoomSurfaceView;
import com.ekumfi.juice.R;
import com.ekumfi.juice.receiver.NetworkReceiver;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.facebook.internal.Utility.deleteDirectory;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;


public class VideoActivity extends AppCompatActivity{

    public static String VIDEO_URL;

    private SimpleExoPlayer player;
    NetworkReceiver networkReceiver;
    public static Activity videoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ZoomLogger.setLogLevel(ZoomLogger.LEVEL_VERBOSE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoActivity = this;
        networkReceiver = new NetworkReceiver();
        final boolean supportsSurfaceView = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
        if (supportsSurfaceView) setUpVideoPlayer();

        final View zoomSurface = findViewById(R.id.zoom_surface);

        if (supportsSurfaceView) {
            player.setPlayWhenReady(true);
            zoomSurface.setVisibility(View.VISIBLE);
        }
        Log.d("sdffds", VIDEO_URL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        ZoomSurfaceView surface = findViewById(R.id.surface_view);
        surface.onPause();
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        ZoomSurfaceView surface = findViewById(R.id.surface_view);
        surface.onResume();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setUpVideoPlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this);
        PlayerControlView controls = findViewById(R.id.player_control_view);
        final ZoomSurfaceView surface = findViewById(R.id.surface_view);
        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                surface.setContentSize(width, height);
            }
        });
        surface.setBackgroundColor(ContextCompat.getColor(this, R.color.background));
        surface.addCallback(new ZoomSurfaceView.Callback() {
            @Override
            public void onZoomSurfaceCreated(@NotNull ZoomSurfaceView view) {
                player.setVideoSurface(view.getSurface());
            }

            @Override
            public void onZoomSurfaceDestroyed(@NotNull ZoomSurfaceView view) { }
        });
        controls.setPlayer(player);
        controls.setShowTimeoutMs(0);
        controls.show();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "ZoomLayoutLib"));
        Uri videoUri = Uri.parse(VIDEO_URL);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri);
        player.prepare(videoSource);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();

        File appDir = new File(getFilesDir() + "/SchoolDirectStudent");
        if (appDir.exists()) {
            deleteDirectory(appDir);
        }
    }

    public static void toastInternetConnectionStatus(Context context, boolean connected) {
        if (connected) {
            Toast.makeText(context, "Connected to internet.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Disconnected from internet.", Toast.LENGTH_SHORT).show();
        }
    }
}
