package com.example.navigation_smd_7a;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 vp2;
    ViewPagerAdapter adapter;
    int count=0;
    boolean flag = false;

    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapter = new ViewPagerAdapter(this);
        vp2 = findViewById(R.id.viewpager2);
        vp2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        fab_add = findViewById(R.id.fab_add);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting custom view for dialog.
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Product");
                View v = LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.add_new_product_dialog_design, null, false);
                dialog.setView(v);
                EditText etTitle = v.findViewById(R.id.etTitle);
                EditText etDate = v.findViewById(R.id.etDate);
                EditText etPrice = v.findViewById(R.id.etPrice);

                dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = etTitle.getText().toString().trim();
                        String date = etDate.getText().toString().trim();
                        String price = etPrice.getText().toString();

                        ProductDB productDB = new ProductDB(MainActivity.this);
                        productDB.open();
                        productDB.insert(title, date, Integer.parseInt(price));
                        productDB.close();
                        Toast.makeText(MainActivity.this, "Product Added", Toast.LENGTH_SHORT).show();

                        NewOrderFragment fragment = (NewOrderFragment) getSupportFragmentManager().findFragmentByTag("f" + 3);
                        if (fragment != null) {
                            fragment.refreshProductList();
                        }
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
            }
        });


        TabLayoutMediator tabLayoutMediator =
                new TabLayoutMediator(tabLayout, vp2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            case 0:
                                tab.setText("Scheduled");
                                tab.setIcon(R.drawable.schedule_icon);
                                BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                                badgeDrawable.setNumber(count);
                                badgeDrawable.setMaxCharacterCount(2);
                                badgeDrawable.setVisible(true);
                                break;
                            case 1:
                                tab.setText("Delivered");
                                tab.setIcon(R.drawable.delivered_icon);
                                break;
                            default:
                                    tab.setText("New Orders");
                                    tab.setIcon(R.drawable.new_orders_icon);
                        }
                    }
                });
        tabLayoutMediator.attach();

        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TabLayout.Tab selectedTab = tabLayout.getTabAt(position);
                BadgeDrawable badgeDrawable = selectedTab.getBadge();
                if(badgeDrawable != null) {
                    count = 0;
                    badgeDrawable.setNumber(count);
                    if (!flag)
                        flag = true;
                    else
                        badgeDrawable.setVisible(false);
                }
            }
        });

    }
}

//Steps to Retrieve Badge Count from Database
//Set up the Database: First, ensure you have a table in the database to store the badge count for each tab (e.g., "Notifications" table).
//
//Create a Database Helper Class: If you're using SQLite, create a helper class to handle database interactions.
//
//Fetch the Badge Count: Write a method to fetch the badge count and call it when setting up or updating the badge.
//
//Display the Badge: After retrieving the badge count, set the badge on the corresponding tab.
//
//Example Code
//Step 1: Setting up SQLite Database
//Create a database helper class that extends SQLiteOpenHelper to handle database creation and retrieval. This example assumes a "Notifications" table that stores the notification count.
//
//java
//Copy code
//public class DatabaseHelper extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "appDatabase";
//    private static final int DATABASE_VERSION = 1;
//
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTable = "CREATE TABLE Notifications (id INTEGER PRIMARY KEY, count INTEGER)";
//        db.execSQL(createTable);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS Notifications");
//        onCreate(db);
//    }
//
//    // Method to get the badge count
//    public int getBadgeCount() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT count FROM Notifications WHERE id = 1", null);
//        int count = 0;
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0);
//        }
//        cursor.close();
//        return count;
//    }
//}
//Step 2: Fetch Badge Count and Update the Badge
//In your Activity or Fragment, use the DatabaseHelper to fetch the badge count and update it in the ViewPager2 callback.
//
//java
//Copy code
//DatabaseHelper dbHelper = new DatabaseHelper(this);
//
//// Fetch badge count from the database
//int badgeCount = dbHelper.getBadgeCount();
//
//vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//    @Override
//    public void onPageSelected(int position) {
//        super.onPageSelected(position);
//        TabLayout.Tab selectedTab = tabLayout.getTabAt(position);
//
//        // Get or create badge
//        BadgeDrawable badgeDrawable = selectedTab.getOrCreateBadge();
//
//        // Set badge number from database count
//        if (badgeDrawable != null) {
//            badgeDrawable.setNumber(badgeCount);
//            badgeDrawable.setVisible(badgeCount > 0); // Show badge only if count > 0
//        }
//    }
//});
//Step 3: Updating Badge Count
//Whenever there's a change in notifications, update the count in the database and refresh the badge count:
//
//java
//Copy code
//public void updateBadgeCount(int newCount) {
//    SQLiteDatabase db = dbHelper.getWritableDatabase();
//    ContentValues values = new ContentValues();
//    values.put("count", newCount);
//    db.update("Notifications", values, "id = ?", new String[] {"1"});
//}
//Explanation
//Database Helper: Manages creating, reading, and updating badge counts.
//Fetching Badge Count: Retrieves the current count from the database and sets it as the badge number.
//Updating Badge Count: Call updateBadgeCount() whenever there's a change, which updates both the database and UI.