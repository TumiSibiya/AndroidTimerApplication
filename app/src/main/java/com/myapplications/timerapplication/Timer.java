package com.myapplications.timerapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;
/*
 *
 *  TimerActivity uses CountDownTimer class to simulate Timer.
 *   this class converts System.currentMillis() to hours, minutes and seconds
 *   then display onto textview
 *
 * program by : @arthur Tumi Sibiya
 *
 * */

public class Timer extends AppCompatActivity {


    public long start_time_in_millis = 0;
    public long time_left_in_millis;//= start_time_in_millis;
    public long endTime;

    EditText hourEditText;
    EditText minuteEditText;

    TextView timerTextView;

    Button buttonStartPauseTimer;
    Button buttonResetTimer;
    Button buttonClearSet;
    Button buttonSet;
    Button buttonNewTime;

    CountDownTimer countDownTimer;
    boolean isRunning;

    ProgressBar timerProgressBar;
    int progressDivider = 100;
    int timerProgressBarTime = (int)start_time_in_millis;

    private String TAG = Timer.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_activity);
        setTitle("Timer");

        Log.d(TAG,"INSIDE CREATE");

        hourEditText = findViewById(R.id.hours_edit_text_id);
        minuteEditText = findViewById(R.id.minute_edit_text_id);

        timerTextView = findViewById(R.id.textviewfortimer);

        buttonStartPauseTimer = findViewById(R.id.buttonStartPauseTimerId);
        buttonResetTimer = findViewById(R.id.buttonRestTimerId);

        buttonClearSet = findViewById(R.id.button_clear_set);

        buttonSet = findViewById(R.id.button_set);

        buttonNewTime = findViewById(R.id.button_NewTime_id);

        timerProgressBar = findViewById(R.id.progressBar);

        //setButtonActions
        buttonStartPauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    if(time_left_in_millis == 0){
                        Toast.makeText(Timer.this, "Time Up ", Toast.LENGTH_SHORT).show();
                        buttonNewTime.setClickable(true);
                    }else{

                        startCountDownTimer();
                    }

                } else
                    pauseCountDownTimer();
            }
        });

        buttonResetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCountDownTimer();
                updateCountDownTimerTextView();
                updateInterface();
            }
        });

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tempHideViewVisibilityChangeNewButtonText();

                String hourString = hourEditText.getText().toString();

                String minuteString = minuteEditText.getText().toString();

                long total;

                if(hourString.length() == 0){


                    if (minuteString.length() == 0) {
                        Toast.makeText(Timer.this, "Invalid Number", LENGTH_SHORT).show();
                        return;

                    }else {

                        total = Long.parseLong(minuteString) * 60000;
                        setTime(total);

                    }

                }else {

                    long hour = Long.parseLong(hourString);

                    hour = hour * 360_000 *10;

                    if(minuteString.length() == 0){

                        total = hour;

                    }else{

                        long minute = Long.parseLong(minuteString) * 60000;

                        total = minute + hour;

                    }
                    setTime(total);
                }

                resetCountDownTimer();
                updateCountDownTimerTextView();
                updateInterface();
            }
        });

        buttonClearSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hourEditText.getText().clear();
                minuteEditText.getText().clear();

                Log.d(TAG, "User cleared Edit text");
            }
        });

        buttonNewTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                tempHideViewVisibilityChangeNewButtonText();

            }
        });
    }

    //button actions
    public void setTime(long millis) {
        start_time_in_millis = millis;

    }

    public void startCountDownTimer() {

        endTime = System.currentTimeMillis() + time_left_in_millis;

        countDownTimer = new CountDownTimer(time_left_in_millis, 1000) {
            @Override
            public void onTick(long timeUntilFinish) {
                time_left_in_millis = timeUntilFinish;

                timerProgressBarTime = (int)(timeUntilFinish / progressDivider);
                timerProgressBar.setProgress(timerProgressBarTime);

                updateCountDownTimerTextView();
                updateInterface();
            }

            @Override
            public void onFinish() {
                timerProgressBar.setProgress(0);
                isRunning = false;
                updateInterface();
            }
        }.start();
        isRunning = true;
    }

    public void pauseCountDownTimer() {
        countDownTimer.cancel();
        //buttonStartPauseTimer.setText(R.string.resumeButtonPress);
        isRunning = false;
        updateInterface();
    }

    public void resetCountDownTimer() {

        time_left_in_millis = start_time_in_millis;

        isRunning = false;

        updateCountDownTimerTextView();
        updateInterface();
    }

    //udate countdown
    public void updateCountDownTimerTextView() {

        int hours = (int) (time_left_in_millis / 1000) / 3600;
        Log.d(TAG, "Hourse returned : " + hours);

        int minutes = (int) ((time_left_in_millis / 1000) % 3600) / 60;
        Log.d(TAG, "Minutes returned :" + minutes);

        int seconds = (int) (time_left_in_millis / 1000) % 60;
        Log.d(TAG, "econdes returned : " + seconds);

        String formattedTimeLeftInMillis;

        if (hours == 0) {
            formattedTimeLeftInMillis = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        } else {
            formattedTimeLeftInMillis = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        }
        timerTextView.setText(formattedTimeLeftInMillis);
    }

    void updateInterface() {

        //if timer is not running do this
        if (!isRunning) {

            //if timer is countdown is not yet finished but paused do this
            if (time_left_in_millis != 0 && countDownTimer != null) {

                buttonStartPauseTimer.setText("Start");
                buttonResetTimer.setVisibility(View.VISIBLE);


            } else {//lest fo this

                buttonStartPauseTimer.setText("Start");
                buttonResetTimer.setVisibility(View.INVISIBLE);

            }

            buttonNewTime.setClickable(true);

        } else {//or else if time is running do this

            buttonStartPauseTimer.setText("Pause");
            buttonResetTimer.setVisibility(View.INVISIBLE);
            buttonNewTime.setClickable(false);

            if (buttonStartPauseTimer.getText().equals("Pause")) {
                //TODO add some cool features on active countdown and buttonStartPause getText(); return Pause and view
            }


        }
        timerProgressBar.setProgress(((int)time_left_in_millis / progressDivider));
        updateCountDownTimerTextView();

    }
    protected  void tempHideViewVisibilityChangeNewButtonText(){


        String buttonContent = buttonNewTime.getText().toString();

        if(buttonContent.equalsIgnoreCase("NEW TIME")){

            timerTextView.setVisibility(View.INVISIBLE);
            buttonStartPauseTimer.setVisibility(View.INVISIBLE);
            buttonResetTimer.setVisibility(View.INVISIBLE);

            buttonSet.setVisibility(View.VISIBLE);
            buttonClearSet.setVisibility(View.VISIBLE);


            hourEditText.setVisibility(View.VISIBLE);
            minuteEditText.setVisibility(View.VISIBLE);

            buttonNewTime.setText("Cancel");


        }else if(buttonContent.equalsIgnoreCase("Cancel")){

            timerTextView.setVisibility(View.VISIBLE);
            buttonStartPauseTimer.setVisibility(View.VISIBLE);
            buttonResetTimer.setVisibility(View.INVISIBLE);

            buttonSet.setVisibility(View.INVISIBLE);;
            buttonClearSet.setVisibility(View.INVISIBLE);


            hourEditText.setVisibility(View.INVISIBLE);
            minuteEditText.setVisibility(View.INVISIBLE);

            buttonNewTime.setText("NEW TIME");

        }
    }

    //this feature requires attentions
    //delete android:screenOrientation="portrait" on Manifiest for default features or go Auto

    //save values and state before on stop
    @Override
    public void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor simpleEditor = prefs.edit();

        simpleEditor.putLong("start_time_in_millis", start_time_in_millis);
        simpleEditor.putLong("time_left_in_millis", time_left_in_millis);
        simpleEditor.putLong("endTime", endTime);
        simpleEditor.putBoolean("isRunning", isRunning);


        simpleEditor.apply();

    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPrefs = getSharedPreferences("prefs", MODE_PRIVATE);

        start_time_in_millis = sharedPrefs.getLong("start_time_in_millis", 0);
        time_left_in_millis = sharedPrefs.getLong("time_left_in_millis", start_time_in_millis);
        isRunning = sharedPrefs.getBoolean("isRunning", false);

        updateCountDownTimerTextView();


        if (isRunning) {
            endTime = sharedPrefs.getLong("endTime", 0);
            time_left_in_millis = endTime - System.currentTimeMillis();
            if (time_left_in_millis <= 0) {

                time_left_in_millis = 0;
                isRunning = false;
            } else {
                updateCountDownTimerTextView();
                startCountDownTimer();
            }
            updateInterface();
        }

    }
}
