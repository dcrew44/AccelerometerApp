package com.example.accelerometerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ImageView beeImageView;
    private Bee bee;
    private SensorManager sensorManager;
    private Sensor mSensorAccelerometer;
    private Thread backgroundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout mainLayout = findViewById(R.id.container);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        beeImageView = (ImageView) inflater.inflate(R.layout.bee_image, null);
        beeImageView.setX(250f);
        beeImageView.setY(250f);
        mainLayout.addView(beeImageView, 0);

        DisplayMetrics screenMetrics = Resources.getSystem().getDisplayMetrics();
        int screenWidth = Math.round(screenMetrics.widthPixels);
        int screenHeight = Math.round(screenMetrics.heightPixels);

        bee = new Bee();
        bee.setBoundaries(0, screenHeight, 0, screenWidth);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        backgroundThread = new Thread(moveCalculations);
    }

    private Runnable moveCalculations = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    bee.move();
                    Thread.sleep(10);
                    handler.sendEmptyMessage(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public  Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            beeImageView.setX((float) bee.mX);
            beeImageView.setY((float) bee.mY);
        }
    };
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            bee.mVelocityX = event.values[0] * 3;
            bee.mVelocityY = event.values[1] * 3;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mSensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        backgroundThread.start();
    }
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, mSensorAccelerometer);
        super.onPause();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
    }


}