package com.example.inventory_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SimpleGridAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final OnRemoveClick removeClick;
    private final OnQuantityChange quantityChange;
    private List<Item> items;



    public SimpleGridAdapter(Context context, OnRemoveClick removeClick, OnQuantityChange quantityChange, List<Item> items) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.removeClick = removeClick;
        this.quantityChange = quantityChange;
        this.items = (items != null) ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void setData(List<Item> newData) {
        items.clear();
        if(newData != null) items.addAll(newData);

        notifyDataSetChanged();
    }

    @Override public int getCount() {return items == null ? 0 : items.size();}
    @Override public Object getItem(int position) {
        return items.get(position);
    }
    @Override public long getItemId(int position) {
        return items.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);

            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Item item = items.get(position);
        vh.name.setText(item.name);
        vh.quantity.setText("Qty: " + item.quantity);

        vh.remove.setOnClickListener(v -> removeClick.onRemove(item));
        vh.decrease.setOnClickListener(v -> quantityChange.onChange(item, -1));
        vh.increase.setOnClickListener(v -> quantityChange.onChange(item, +1));

        return convertView;
    }

    static class ViewHolder {
        final TextView name, quantity;
        final Button remove, decrease, increase;

        ViewHolder(View v) {
            name = v.findViewById(R.id.item_name);
            quantity = v.findViewById(R.id.item_quantity);
            remove = v.findViewById(R.id.remove_button);
            decrease = v.findViewById(R.id.decrease_button);
            increase = v.findViewById(R.id.increase_button);
        }
    }
}
