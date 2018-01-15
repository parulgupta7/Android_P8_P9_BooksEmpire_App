package com.example.parul.booksempire;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parul.booksempire.data.BooksContract.BooksEntry;

public class BooksCursorAdapter extends CursorAdapter {

    public BooksCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView bookName = (TextView) view.findViewById(R.id.book_name);
        TextView bookPrice = (TextView) view.findViewById(R.id.book_price);
        TextView bookQuantity = (TextView) view.findViewById(R.id.book_quantity);
        bookName.setText(cursor.getString(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_PRODUCT_NAME)));
        bookPrice.setText(cursor.getFloat(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_PRODUCT_PRICE)) + "");
        bookQuantity.setText(cursor.getInt(cursor.getColumnIndexOrThrow(BooksEntry.COLUMN_PRODUCT_QUANTITY)) + "");
        final Button sale = (Button) view.findViewById(R.id.sale);
        final long id = cursor.getLong(cursor.getColumnIndex(BooksEntry._ID));

        sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) view.findViewById(R.id.book_quantity);
                Integer oldQuantity = Integer.parseInt(textView.getText().toString());
                oldQuantity--;
                if (oldQuantity < 0) {
                    Toast.makeText(context, R.string.validate_negative_sale, Toast.LENGTH_SHORT).show();
                    return;
                }

                textView.setText(String.valueOf(oldQuantity));
                Uri contentUri = ContentUris.withAppendedId(BooksEntry.CONTENT_URI, id);
                ContentValues contentValues = new ContentValues();
                contentValues.put(BooksEntry.COLUMN_PRODUCT_QUANTITY, oldQuantity);
                context.getContentResolver().update(contentUri, contentValues, null, null);
            }
        });

    }
}
