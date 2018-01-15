package com.example.parul.booksempire.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.parul.booksempire.data.BooksContract.BooksEntry;

public class BooksDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "booksOnly";
    private static final int DATABASE_VERSION = 1;

    public BooksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static String INSERT_QUERY = "CREATE TABLE " + BooksEntry.TABLE_NAME + "(" +
            BooksEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BooksEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
            BooksEntry.COLUMN_PRODUCT_PRICE + " REAL NOT NULL," +
            BooksEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL DEFAULT 0," +
            BooksEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
            BooksEntry.COLUMN_SUPPLIER_EMAIL + " TEXT," +
            BooksEntry.COLUMN_SUPPLIER_PHONENUMBER + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INSERT_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
