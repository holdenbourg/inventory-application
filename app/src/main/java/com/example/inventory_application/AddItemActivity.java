package com.example.inventory_application;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddItemActivity extends AppCompatActivity {
    private AppDatabase db;
    private final ExecutorService es = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        db = AppDatabase.get(this);

        EditText itemNameInput = findViewById(R.id.item_name_text);
        EditText quantityInput = findViewById(R.id.quantity_text);

        Button addItemButton = findViewById(R.id.add_item_final_button);

        addItemButton.setOnClickListener(v -> {
            String name = itemNameInput.getText().toString().trim();
            String quantityString = quantityInput.getText().toString().trim();
            int quantity = quantityString.isEmpty() ? 0 : Integer.parseInt(quantityString);

            if(name.isEmpty() || quantityString.isEmpty()) {
                Toast.makeText(this, "Item name and quantity required", Toast.LENGTH_SHORT).show();

                return;
            }

            es.execute(() -> {
                Item newItem = new Item();

                newItem.name = name;
                newItem.quantity = quantity;

                db.itemDao().insert(newItem);

                runOnUiThread(() -> {
                    Toast.makeText(this, name + " was added to the inventory", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
