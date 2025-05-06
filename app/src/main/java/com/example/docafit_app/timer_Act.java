package com.example.docafit_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class timer_Act extends AppCompatActivity {

    private TextView countdownText;
    private Button startButton;
    private Button finishButton;

    private TextView tvMinutes, tvSeconds;
    private Button btnIncreaseMinutes, btnDecreaseMinutes, btnIncreaseSeconds, btnDecreaseSeconds;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_timer);

        countdownText = findViewById(R.id.countdown_text);
        startButton = findViewById(R.id.start_button);
        finishButton = findViewById(R.id.finish_button);

        tvMinutes = findViewById(R.id.show_minutes);
        tvSeconds = findViewById(R.id.show_seconds);
        btnIncreaseMinutes = findViewById(R.id.plus_minutes_button);
        btnDecreaseMinutes = findViewById(R.id.minus_minutes_button);
        btnIncreaseSeconds = findViewById(R.id.plus_seconds_button);
        btnDecreaseSeconds = findViewById(R.id.minus_seconds_button);

        updateMinuteSecondViews();

        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                startTimer();
            }
        });

        finishButton.setOnClickListener(v -> {
            Intent intent = new Intent(timer_Act.this, mainPage_Act.class);
            startActivity(intent);
            finish();
        });

        btnIncreaseMinutes.setOnClickListener(v -> {
            timeLeftInMillis += 60000;
            updateMinuteSecondViews();
        });

        btnDecreaseMinutes.setOnClickListener(v -> {
            if (timeLeftInMillis >= 60000) {
                timeLeftInMillis -= 60000;
                updateMinuteSecondViews();
            }
        });

        btnIncreaseSeconds.setOnClickListener(v -> {
            if ((timeLeftInMillis / 1000) % 60 < 59) {
                timeLeftInMillis += 1000;
            } else {
                timeLeftInMillis += (1000 - ((timeLeftInMillis / 1000) % 60) * 1000);
            }
            updateMinuteSecondViews();
        });

        btnDecreaseSeconds.setOnClickListener(v -> {
            if (timeLeftInMillis >= 1000) {
                timeLeftInMillis -= 1000;
                updateMinuteSecondViews();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateMinuteSecondViews();
            }

            public void onFinish() {
                isRunning = false;
                countdownText.setText(getString(R.string.time_up));
            }
        }.start();

        isRunning = true;
    }

    private void updateMinuteSecondViews() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        tvMinutes.setText(String.format("%02d", minutes));
        tvSeconds.setText(String.format("%02d", seconds));
    }
}
