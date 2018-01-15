package com.example.parul.booksempire;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import com.example.parul.booksempire.data.BooksContract.BooksEntry;

public class view_book extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BOOK_LOADER = 1;
    Uri bookContentUri;
    private TextView tBookPrice;
    private TextView tBookName;
    private TextView tQuantity;
    private TextView tSupplierName;
    private TextView tSupplierEmail;
    private TextView tSupplierPhone;
    private Button order;
    private Button editBook;
    private Button deleteBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        tBookName = (TextView) findViewById(R.id.book_name_textView);
        tBookPrice = (TextView) findViewById(R.id.book_price_textView);
        tQuantity = (TextView) findViewById(R.id.books_quantity);
        tSupplierName = (TextView) findViewById(R.id.supplier_name_textView);
        tSupplierEmail = (TextView) findViewById(R.id.supplier_email_textView);
        tSupplierPhone = (TextView) findViewById(R.id.supplier_phone_textView);
        order = (Button) findViewById(R.id.viewBook_order_button);
        editBook = (Button) findViewById(R.id.editBook_button);
        deleteBook = (Button) findViewById(R.id.deleteBook_button);

        bookContentUri = getIntent().getData();
        if (bookContentUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(BOOK_LOADER, null, this);
        }

    }

    private void deleteThisBookData() {
        if (bookContentUri != null) {
            int rowsDeleted = getContentResolver().delete(bookContentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.unsuccessful_deletion),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successful_deletion),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deletePopUp(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_warning);
        builder.setPositiveButton(R.string.delete_book, discardButtonClickListener);
        builder.setNegativeButton(R.string.dont_delete_book, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int id) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(view_book.this);
                this.finish();
                ;
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void decrementQuantity(View v) {
        TextView quantityView = (TextView) findViewById(R.id.books_quantity);
        int bookQuantity = Integer.valueOf(quantityView.getText().toString());
        if (--bookQuantity < 0) {
            Toast.makeText(getBaseContext(), R.string.validate_negative_sale,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(BooksEntry.COLUMN_PRODUCT_QUANTITY, bookQuantity);
        int rowsAffectedNumber = getContentResolver().update(bookContentUri, contentValues, null, null);

        if (rowsAffectedNumber == 0) {
            Toast.makeText(this, getString(R.string.unsuccessful_quantity_decrement),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.successful_quantity_decrement),
                    Toast.LENGTH_SHORT).show();
        }

        quantityView.setText(String.valueOf(bookQuantity));
    }

    public void incrementQuantity(View v) {
        TextView quantityView = (TextView) findViewById(R.id.books_quantity);
        int bookQuantity = Integer.valueOf(quantityView.getText().toString());
        quantityView.setText(String.valueOf(++bookQuantity));

        ContentValues contentValues = new ContentValues();
        contentValues.put(BooksEntry.COLUMN_PRODUCT_QUANTITY, bookQuantity);
        int rowsAffected = getContentResolver().update(bookContentUri, contentValues, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(this, getString(R.string.unsuccessful_quantity_increment),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.successful_quantity_increment),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_menu_items, menu);
        return true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        final StringBuilder orderString = new StringBuilder();
        if (cursor.moveToFirst()) {
            String bookName = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_NAME));
            tBookName.setText(bookName);
            orderString.append(bookName + "\n");

            String bookPrice = cursor.getDouble(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_PRICE)) + "";
            tBookPrice.setText(bookPrice);
            orderString.append(bookPrice + "\n");

            String bookQuantity = cursor.getInt(cursor.getColumnIndex(BooksEntry.COLUMN_PRODUCT_QUANTITY)) + "";
            tQuantity.setText(bookQuantity);
            orderString.append(bookQuantity + "\n");

            String suppName = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_NAME));
            tSupplierName.setText(suppName);
            orderString.append(suppName + "\n");

            String suppEmail = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_EMAIL));
            tSupplierEmail.setText(suppEmail);
            orderString.append(suppEmail);

            String suppPhone = cursor.getString(cursor.getColumnIndex(BooksEntry.COLUMN_SUPPLIER_PHONENUMBER));
            tSupplierPhone.setText(suppPhone);
            orderString.append(suppPhone);
        }
        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Here are the Book details : ");
                intent.putExtra(Intent.EXTRA_TEXT, (Serializable) orderString);
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException exception) {
                    Toast.makeText(getBaseContext(), R.string.unsuccessful_email_send, Toast.LENGTH_SHORT).show();
                }
            }
        });

        editBook.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view_book.this, edit_book.class);
                String s = bookContentUri.toString();
                intent.setData(Uri.parse(s));
                startActivity(intent);
            }
        });

        deleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener deleteClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteThisBookData();
                                finish();
                            }
                        };
                deletePopUp(deleteClickListener);
            }
        });

    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        String[] projections = {
                BooksEntry.COLUMN_PRODUCT_NAME,
                BooksEntry.COLUMN_PRODUCT_PRICE,
                BooksEntry.COLUMN_PRODUCT_QUANTITY,
                BooksEntry.COLUMN_SUPPLIER_NAME,
                BooksEntry.COLUMN_SUPPLIER_EMAIL,
                BooksEntry.COLUMN_SUPPLIER_PHONENUMBER,
        };
        return new CursorLoader(this, bookContentUri, projections, null, null, null);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        tBookName.setText("");
        tBookPrice.setText("");
        tQuantity.setText("0");
        tSupplierName.setText("");
        tSupplierEmail.setText("");
        tSupplierPhone.setText("");
    }
}
