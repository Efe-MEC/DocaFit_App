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

        updateCountdownText();

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
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            public void onFinish() {
                isRunning = false;
                countdownText.setText("Süre doldu!");
            }
        }.start();

        isRunning = true;
    }

    private void updateCountdownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("Kalan süre: %02d:%02d", minutes, seconds);
        countdownText.setText(timeFormatted);
    }
}
