package com.example.parul.booksempire.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

import java.util.regex.Pattern;

import com.example.parul.booksempire.data.BooksContract.BooksEntry;

public class BooksProvider extends ContentProvider {

    BooksDbHelper dbHelper;
    private static final int booksNumber = 1000;
    private static final int bookId = 1001;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(BooksEntry.AUTHORIZE_CONTENT, BooksEntry.BOOKS_PATH, booksNumber);
        uriMatcher.addURI(BooksEntry.AUTHORIZE_CONTENT, BooksEntry.BOOKS_PATH + "/#", bookId);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int matchData = uriMatcher.match(uri);
        switch (matchData) {
            case booksNumber:
                return insertBookData(uri, contentValues);
            default:
                throw new IllegalArgumentException("for" + uri + ", Insertion is not Supported");
        }
    }

    private Uri insertBookData(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long dataId = db.insert(BooksEntry.TABLE_NAME, null, contentValues);
        if (dataId == -1) {
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, dataId);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new BooksDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int matchDataId = uriMatcher.match(uri);
        switch (matchDataId) {
            case booksNumber:
                return BooksEntry.LIST_TYPE_CONTENT;
            case bookId:
                return BooksEntry.ITEM_TYPE_CONTENT;
            default:
                throw new IllegalStateException("This URI : " + uri + " , is not known with match " + matchDataId);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        int matchDataId = uriMatcher.match(uri);
        switch (matchDataId) {
            case booksNumber:
                cursor = db.query(BooksEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case bookId:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(BooksEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("The URI : " + uri + " , cannot be Queried because this is unkown URI ");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int matchDataId = uriMatcher.match(uri);
        switch (matchDataId) {
            case booksNumber:
                return updateBookData(uri, contentValues, selection, selectionArgs);
            case bookId:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBookData(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("For" + uri + ", Update is not supported");
        }
    }

    private int updateBookData(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (contentValues.size() == 0) {
            return 0;
        }

        int updatedRowsNumber = db.update(BooksEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (updatedRowsNumber != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRowsNumber;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int matchDataId = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (matchDataId) {
            case booksNumber:
                return db.delete(BooksEntry.TABLE_NAME, selection, selectionArgs);
            case bookId:
                selection = BooksEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int deletedRowsNumber = db.delete(BooksEntry.TABLE_NAME, selection, selectionArgs);
                if (deletedRowsNumber != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deletedRowsNumber;
            default:
                throw new IllegalArgumentException("For" + uri + ", Deletion is not supported");
        }
    }
}
