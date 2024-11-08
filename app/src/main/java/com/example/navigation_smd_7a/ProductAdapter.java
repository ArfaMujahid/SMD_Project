package com.example.navigation_smd_7a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;
    int resource;
    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null)
        {
            v = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView tvTitle = v.findViewById(R.id.tvProductTitle);
        ImageView ivEdit = v.findViewById(R.id.ivEdit);
        ImageView ivDelete = v.findViewById(R.id.ivDelete);

        Product p = getItem(position);
        tvTitle.setText(p.getPrice()+" : "+p.getTitle());

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Edit Product");

                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_product, null);
                dialog.setView(dialogView);

                EditText etTitle = dialogView.findViewById(R.id.etEditTitle);
                EditText etPrice = dialogView.findViewById(R.id.etEditPrice);

                etTitle.setText(p.getTitle());
                etPrice.setText(String.valueOf(p.getPrice()));

                dialog.setPositiveButton("Save", (dialogInterface, i) -> {
                    String title = etTitle.getText().toString().trim();
                    int price = Integer.parseInt(etPrice.getText().toString().trim());

                    ProductDB productDB = new ProductDB(context);
                    productDB.open();
                    productDB.updatePrice(p.getId(), price);
                    productDB.close();

                    p.setTitle(title);
                    p.setPrice(price);

                    notifyDataSetChanged();

                    Toast.makeText(context, "Product Updated", Toast.LENGTH_SHORT).show();
                });

                dialog.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

                dialog.show();
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDB db = new ProductDB(context);
                db.open();
                db.remove(p.getId());
                db.close();
                remove(p);
                notifyDataSetChanged();
            }
        });

        return v;
    }
}
