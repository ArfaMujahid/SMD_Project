package com.example.navigation_smd_7a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductDB {
    public final String DATABASE_NAME = "products_db";
    public final String DATABASE_TABLE_NAME = "products";
    public final String KEY_ID = "id";
    public final String KEY_TITLE = "title";
    public final String KEY_DATE = "date";
    public final String KEY_PRICE = "price";
    public final String KEY_STATUS = "status";

    private final int DB_VERSION = 1;
    Context context;
    DBHelper dbHelper;

    ProductDB(Context context)
    {
        this.context = context;
    }

    public void open()
    {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void close()
    {
        dbHelper.close();
    }

    public long insert(String title, String date, int price)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_DATE, date);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_STATUS, "new");

        return db.insert(DATABASE_TABLE_NAME, null, cv);
    }

    public int remove(int id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME, KEY_ID+"=?", new String[]{id+""});
    }

    public int updatePrice(int id, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRICE, price);
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID+"=?", new String[]{id+""});
    }

    public ArrayList<Product> fetchProducts()
    {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();
        String []columns = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE};

        Cursor cursor = readDb.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
        if(cursor!=null) {

            int id_index = cursor.getColumnIndex(KEY_ID);
            int title_index = cursor.getColumnIndex(KEY_TITLE);
            int date_index = cursor.getColumnIndex(KEY_DATE);
            int price_index = cursor.getColumnIndex(KEY_PRICE);
            while (cursor.moveToNext()) {
                Product p = new Product(cursor.getInt(id_index), cursor.getString(title_index), cursor.getString(date_index),
                        cursor.getInt(price_index), "");
                products.add(p);
            }
            cursor.close();
        }
        return products;

    }

    public ArrayList<Product> fetchProductsWithStatus(String status) {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        ArrayList<Product> products = new ArrayList<>();
        String[] columns = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE};

        Cursor cursor = readDb.query(DATABASE_TABLE_NAME, columns, KEY_STATUS + "=?", new String[]{status}, null, null, null);
        if (cursor != null) {
            int id_index = cursor.getColumnIndex(KEY_ID);
            int title_index = cursor.getColumnIndex(KEY_TITLE);
            int date_index = cursor.getColumnIndex(KEY_DATE);
            int price_index = cursor.getColumnIndex(KEY_PRICE);
            while (cursor.moveToNext()) {
                Product p = new Product(cursor.getInt(id_index), cursor.getString(title_index), cursor.getString(date_index),
                        cursor.getInt(price_index), "");
                products.add(p);
            }
            cursor.close();
        }
        return products;
    }

    public void markAsDelivered(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, "delivered");
        db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{String.valueOf(productId)});
    }

    private class DBHelper extends SQLiteOpenHelper
    {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String query = "CREATE TABLE IF NOT EXISTS "+DATABASE_TABLE_NAME+"("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +KEY_TITLE+" TEXT NOT NULL,"+KEY_DATE+" TEXT NOT NULL,"+KEY_PRICE+" INTEGER, " +KEY_STATUS +");";
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

}

//package com.example.navigation_smd_7a;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.ArrayList;
//
//public class ProductDB extends SQLiteOpenHelper {
//
//    public static final String DATABASE_NAME = "products_db";
//    public static final String DATABASE_TABLE_NAME = "products";
//    public static final String KEY_ID = "id";
//    public static final String KEY_TITLE = "title";
//    public static final String KEY_DATE = "date";
//    public static final String KEY_PRICE = "price";
//    public static final String KEY_STATUS = "status";
//    private static final int DB_VERSION = 1;
//
//    public ProductDB(Context context) {
//        super(context, DATABASE_NAME, null, DB_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // SQL statement to create the table
//        String query = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME + "("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + KEY_TITLE + " TEXT NOT NULL, "
//                + KEY_DATE + " TEXT NOT NULL, "
//                + KEY_PRICE + " INTEGER, "
//                + KEY_STATUS + " TEXT);";
//        db.execSQL(query);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
//        onCreate(db);
//    }
//
//    public long insert(String title, String date, int price) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(KEY_TITLE, title);
//        cv.put(KEY_DATE, date);
//        cv.put(KEY_PRICE, price);
//        cv.put(KEY_STATUS, "new");
//
//        return db.insert(DATABASE_TABLE_NAME, null, cv);
//    }
//
//    public int remove(int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete(DATABASE_TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(id)});
//    }
//
//    public int updatePrice(int id, int price) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(KEY_PRICE, price);
//        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{String.valueOf(id)});
//    }
//
//    public ArrayList<Product> fetchProducts() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        ArrayList<Product> products = new ArrayList<>();
//        String[] columns = {KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE};
//
//        Cursor cursor = db.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
//                String title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
//                String date = cursor.getString(cursor.getColumnIndex(KEY_DATE));
//                int price = cursor.getInt(cursor.getColumnIndex(KEY_PRICE));
//                products.add(new Product(id, title, date, price, ""));
//            }
//            cursor.close();
//        }
//        return products;
//    }
//}
