package com.example.inventory_application;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private AppDatabase db;
    private final ExecutorService es = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        db = AppDatabase.get(this);

        EditText firstNameInput = findViewById(R.id.first_name_input);
        EditText lastNameInput = findViewById(R.id.last_name_input);
        EditText emailInput = findViewById(R.id.email_input);
        EditText usernameInput = findViewById(R.id.register_username_input);
        EditText passwordInput = findViewById(R.id.register_password_input);

        Button regsiterButton = findViewById(R.id.register_account_button);

        regsiterButton.setOnClickListener(v -> {
            String first = firstNameInput.getText().toString().trim();
            String last = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString();

            if(first.isEmpty() || last.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields must be filled in", Toast.LENGTH_SHORT).show();

                return;
            }

            es.execute(() -> {
                try {
                    User newUser = new User();
                    newUser.firstName = first;
                    newUser.lastName = last;
                    newUser.email = email;
                    newUser.username = username ;
                    newUser.password = password;

                    db.userDao().insert(newUser);

                    runOnUiThread(() -> {
                        Toast.makeText(this, first + "'s account was created", Toast.LENGTH_SHORT).show();
                        finish();
                    });

                } catch (Exception exception) {
                    // if an exception is caught it is most likely a non-unqieu username or email
                    runOnUiThread(() -> {
                        Toast.makeText(this, "The username or email provided already exists", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }
}
