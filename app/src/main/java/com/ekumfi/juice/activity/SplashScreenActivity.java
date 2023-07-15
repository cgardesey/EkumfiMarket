package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ekumfi.juice.R;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmProduct;
import com.ekumfi.juice.realm.RealmUser;
import com.ekumfi.juice.util.RealmUtility;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView imageView = findViewById(R.id.logotext);
        ImageView logo = findViewById(R.id.logo);

        Glide.with(this)
                .asGif()
                .load(R.drawable.superfixlogogif)

                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        SplashScreenActivity.this.runOnUiThread(() -> {

                            Timer myTimer = new Timer();

                            myTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    // If you want to modify a view in your Activity
                                    SplashScreenActivity.this.runOnUiThread(() -> {
                                        logo.setImageDrawable(null);
                                        Glide.with(getApplicationContext())
                                                .asGif()
                                                .load(R.drawable.staticsuperfixlogogif)

                                                .listener(new RequestListener<GifDrawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        Timer myTimer = new Timer();

                                                        myTimer.schedule(new TimerTask() {
                                                            @Override
                                                            public void run() {
                                                                // If you want to modify a view in your Activity
                                                                Realm.init(getApplicationContext());
                                                                boolean signedIn = !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + MYUSERID,  "").equals("") && Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmProduct.class).findFirst() != null;
                                                                if (signedIn) {
                                                                    String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
                                                                    if (role.equals("CONSUMER")) {
                                                                            startActivity(new Intent(SplashScreenActivity.this, ConsumerHomeActivity.class));
                                                                    } else if (role.equals("SELLER")) {
                                                                            startActivity(new Intent(SplashScreenActivity.this, SellerHomeActivity.class));
                                                                    } else {
                                                                        startActivity(new Intent(SplashScreenActivity.this, SelectRoleActivity.class));
                                                                    }
                                                                } else {
                                                                    startActivity(new Intent(SplashScreenActivity.this, GetPhoneNumberActivity.class));
                                                                }
                                                                finish();
                                                            }
                                                        }, 1500);
                                                        return false;
                                                    }
                                                })
                                                .into(imageView);

                                    });
                                }
                            }, 2000);
                        });
                        return false;
                    }
                })
                .into(imageView);
    }
}
