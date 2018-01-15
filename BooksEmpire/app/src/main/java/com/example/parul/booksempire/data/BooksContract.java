package com.example.parul.booksempire.data;

import android.provider.BaseColumns;
import android.content.ContentResolver;
import android.net.Uri;

public class BooksContract {
    private BooksContract() {
    }

    public static final class BooksEntry implements BaseColumns {
        public final static String TABLE_NAME = "books";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRODUCT_PRICE = "price";
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "suppName";
        public final static String COLUMN_SUPPLIER_EMAIL = "suppEmail";
        public final static String COLUMN_SUPPLIER_PHONENUMBER = "suppPhone";
        public static final String AUTHORIZE_CONTENT = "com.example.android.books";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORIZE_CONTENT);
        public static final String BOOKS_PATH = "books";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, BOOKS_PATH);

        public static final String LIST_TYPE_CONTENT =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORIZE_CONTENT + "/" + BOOKS_PATH;

        public static final String ITEM_TYPE_CONTENT =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORIZE_CONTENT + "/" + BOOKS_PATH;
    }
}
