package com.example.sorryformath;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.buttonRegister);

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!username.isEmpty() && !password.isEmpty()) {
                new RegisterUserTask().execute(username, password);
            } else {
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class RegisterUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            try {
                // Путь до PHP-обработчика
                URL url = new URL("http://10.0.2.2/phpserver/register.php");

                String postData = "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                // Чтение ответа
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();

            } catch (Exception e) {
                return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject json = new JSONObject(result);
                String status = json.getString("status");

                if ("success".equals(status)) {
                    Toast.makeText(RegistrationActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    finish(); // Возврат назад
                } else {
                    String message = json.optString("message", "Ошибка регистрации");
                    Toast.makeText(RegistrationActivity.this, "Ошибка: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(RegistrationActivity.this, "Неверный ответ от сервера", Toast.LENGTH_LONG).show();
            }
        }
    }
}
