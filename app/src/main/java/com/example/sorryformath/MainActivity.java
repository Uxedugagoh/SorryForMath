package com.example.sorryformath;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnStartGame, btnLogout, btnQuit;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnLogout = findViewById(R.id.btnLogout);
        btnQuit = findViewById(R.id.btnQuit);

        username = getIntent().getStringExtra("username");

        fetchRecord();

        btnStartGame.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, GameActivity.class);
            i.putExtra("username", username);
            startActivity(i);
        });

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        btnQuit.setOnClickListener(v -> finishAffinity());
    }

    void fetchRecord() {
        AsyncTask.execute(() -> {
            try {
                URL url = new URL("http://10.0.2.2/phpserver/get_record.php?username=" + username);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = reader.readLine();
                runOnUiThread(() -> tvWelcome.setText("Welcome, " + username + "! Record: " + line));
            } catch (Exception e) {
                runOnUiThread(() -> tvWelcome.setText("Welcome, " + username + "!"));
            }
        });
    }
}
