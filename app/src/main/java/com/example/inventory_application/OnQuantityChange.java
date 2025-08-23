package com.example.inventory_application;

public interface OnQuantityChange {
    void onChange(Item item, int amount);
}
