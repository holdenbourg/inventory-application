package com.example.inventory_application;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryActivity extends AppCompatActivity {
    private AppDatabase db;
    private final ExecutorService es = Executors.newSingleThreadExecutor();

    private SimpleGridAdapter adapter;
    private GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        NotificationHelper.ensureChannel(this);

        if(Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        db = AppDatabase.get(this);

        grid = findViewById(R.id.inventory_grid);
        Button addButton = findViewById(R.id.add_item_button);

        adapter = new SimpleGridAdapter(
                this,
                item -> es.execute(() -> {
                    db.itemDao().deleteById(item.id);
                    loadItems();
                }),
                (item, amount) -> es.execute(() -> {
                    Item before = db.itemDao().getById(item.id);
                    db.itemDao().changeQuantity(item.id, amount);
                    Item after = db.itemDao().getById(item.id);

                    if (before != null && after != null && amount < 0 && before.quantity > 0 && after.quantity == 0) {
                        NotificationHelper.notifyWhenQuantityReachesZero(getApplicationContext(), after.name);
                    }

                    loadItems();
                }),
                new ArrayList<>()
        );
        grid.setAdapter(adapter);

        addButton.setOnClickListener(v ->
            startActivity(new Intent(InventoryActivity.this, AddItemActivity.class))
        );
    }

    @Override protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        es.execute(() -> {
            List<Item> exampleItems = db.itemDao().getAll();

            if(exampleItems.isEmpty()) {
                createExampleItems();
                exampleItems = db.itemDao().getAll();
            }

            List<Item> finalItems = exampleItems;
            runOnUiThread(() -> adapter.setData(finalItems));
        });
    }

    private void createExampleItems() {
        Item item1 = new Item();
        item1.name = "Motor";
        item1.quantity = 1;

        db.itemDao().insert(item1);

        Item item2 = new Item();
        item2.name = "Engine";
        item2.quantity = 2;

        db.itemDao().insert(item2);

        Item item3 = new Item();
        item3.name = "Starter";
        item3.quantity = 3;

        db.itemDao().insert(item3);

        Item item4 = new Item();
        item4.name = "Steering Wheel";
        item4.quantity = 4;

        db.itemDao().insert(item4);

        Item item5 = new Item();
        item5.name = "Touch Screen";
        item5.quantity = 5;

        db.itemDao().insert(item5);
    }
}
