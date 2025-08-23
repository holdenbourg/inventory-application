package com.example.inventory_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    private final ExecutorService es = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = AppDatabase.get(this);

        EditText usernameInput = findViewById(R.id.username_input);
        EditText passwordInput = findViewById(R.id.password_input);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if(username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out both username and password", Toast.LENGTH_SHORT).show();

                return;
            }

            es.execute(() -> {
                User user = db.userDao().login(username, password);

                runOnUiThread(() -> {
                    if(user != null) {
                        // means the sing in was a success (navigate to the inventory screen)
                        Intent intent = new Intent(LoginActivity.this, InventoryActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
