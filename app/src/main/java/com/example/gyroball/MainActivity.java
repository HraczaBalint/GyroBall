package com.example.gyroball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private BallView ballView;
    private FrameLayout mainFrame;

    private int screenWidth;
    private int screenHeight;
    private PointF ballPosition;
    private PointF ballSpeed;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(0xFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();

        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                ballSpeed.x = -event.values[0];
                ballSpeed.y = event.values[1];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        },sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void init() {
        mainFrame = findViewById(R.id.mainFrame);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        ballPosition = new PointF();
        ballSpeed = new PointF();

        ballPosition.x = screenWidth/2f;
        ballPosition.y = screenHeight/2f;

        ballSpeed.x = 0;
        ballSpeed.y = 0;

        ballView = new BallView(this, ballPosition.x, ballPosition.y, 35, Color.YELLOW);
        mainFrame.addView(ballView);
        ballView.invalidate();
    }

    @Override
    protected void onResume() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ballPosition.x += ballSpeed.x;
                ballPosition.y += ballSpeed.y;

                if (ballPosition.x > screenWidth){
                    ballPosition.x = 0;
                } else if (ballPosition.x < 0){
                    ballPosition.x = screenWidth;
                }

                if (ballPosition.y > screenHeight){
                    ballPosition.y = 0;
                } else if (ballPosition.y < 0){
                    ballPosition.y = screenHeight;
                }

                ballView.x = ballPosition.x;
                ballView.y = ballPosition.y;
                ballView.invalidate();
            }
        };
        timer.schedule(task, 10, 10);
        super.onResume();
    }

    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }
}