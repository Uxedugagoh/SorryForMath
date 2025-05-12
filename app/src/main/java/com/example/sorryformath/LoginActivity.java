package com.example.sorryformath;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // URL к скрипту login.php
                URL url = new URL("http://10.0.2.2/phpserver/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "username=" + username + "&password=" + password;
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                Scanner in = new Scanner(conn.getInputStream());
                StringBuilder result = new StringBuilder();
                while (in.hasNext()) result.append(in.nextLine());
                in.close();

                JSONObject json = new JSONObject(result.toString());
                String status = json.getString("status");

                switch (status) {
                    case "success":
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.putExtra("username", username);
                        startActivity(i);
                        finish();
                        break;
                    case "invalid":
                        Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                        break;
                    case "not_found":
                        Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка подключения", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrationActivity.class));
        });
    }
}
