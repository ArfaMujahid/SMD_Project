package com.example.navigation_smd_7a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class DeliveredAdapter extends ArrayAdapter<Product>{
    private final Context context;
    private final List<Product> products;
    private final ProductDB productDB;

    public DeliveredAdapter(Context context, List<Product> products) {
        super(context, R.layout.list_item_product_sch, products);
        this.context = context;
        this.products = products;
        this.productDB = new ProductDB(context);
        productDB.open();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_project_deli, parent, false);
        }

        Product product = products.get(position);

        TextView titleTextView = convertView.findViewById(R.id.productTitle);
        TextView dateTextView = convertView.findViewById(R.id.productDate);
        TextView priceTextView = convertView.findViewById(R.id.productPrice);

        titleTextView.setText(product.getTitle());
        dateTextView.setText(product.getDate());
        priceTextView.setText(String.valueOf(product.getPrice()));

        return convertView;
    }
}


