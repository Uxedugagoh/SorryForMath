package com.example.sorryformath;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView tvQuestion, tvScore, tvTimer;
    Button[] answerButtons = new Button[4];
    int correctAnswer, score = 0;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvTimer = findViewById(R.id.tvTimer);
        username = getIntent().getStringExtra("username");

        answerButtons[0] = findViewById(R.id.btnOption1);
        answerButtons[1] = findViewById(R.id.btnOption2);
        answerButtons[2] = findViewById(R.id.btnOption3);
        answerButtons[3] = findViewById(R.id.btnOption4);

        for (Button btn : answerButtons) {
            btn.setOnClickListener(this::checkAnswer);
        }

        new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Осталось: " + millisUntilFinished / 1000 + " сек");
            }

            public void onFinish() {
                updateRecord();
                showResultDialog();
            }
        }.start();

        generateQuestion();
    }

    void generateQuestion() {
        Random rand = new Random();
        int a = rand.nextInt(10);
        int b = rand.nextInt(10);
        correctAnswer = a + b;
        tvQuestion.setText(a + " + " + b + " = ?");
        int correctPos = rand.nextInt(4);

        for (int i = 0; i < 4; i++) {
            if (i == correctPos) {
                answerButtons[i].setText(String.valueOf(correctAnswer));
            } else {
                int wrong;
                do {
                    wrong = rand.nextInt(20);
                } while (wrong == correctAnswer);
                answerButtons[i].setText(String.valueOf(wrong));
            }
        }
    }

    void checkAnswer(View view) {
        Button clicked = (Button) view;
        int selected = Integer.parseInt(clicked.getText().toString());
        if (selected == correctAnswer) {
            score++;
            tvScore.setText("Score: " + score);
        }
        generateQuestion();
    }

    void updateRecord() {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/update_record.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                String data = "username=" + username + "&score=" + score;

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                writer.close();
                conn.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    void showResultDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Result")
                .setMessage("Score: " + score)
                .setPositiveButton("Try again", (dialog, which) -> recreate())
                .setNegativeButton("Back to main menu", (dialog, which) -> {
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("line", score);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    void fetchRecord(String username) {

    }
}
