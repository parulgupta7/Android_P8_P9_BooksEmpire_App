package com.example.parul.booksempire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parul.booksempire.data.BooksContract.BooksEntry;

public class add_book extends AppCompatActivity {

    private static final Double DEFAULT_PRODUCT_PRICE = -1.2;
    private EditText eBookPrice;
    private EditText eBookName;
    private EditText eQuantity;
    private EditText eSupplierName;
    private EditText eSupplierEmail;
    private EditText eSupplierPhone;
    private boolean productChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            productChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Button plusButton = (Button) findViewById(R.id.plus_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText quantity = (EditText) findViewById(R.id.books_quantity);
                int value = Integer.valueOf(quantity.getText().toString());
                value++;
                quantity.setText(value + "");
            }
        });

        Button minusButton = (Button) findViewById(R.id.minus_button);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText quantity = (EditText) findViewById(R.id.books_quantity);
                int value = Integer.valueOf(quantity.getText().toString());
                value--;
                if (value < 0) {
                    Toast.makeText(getBaseContext(), R.string.validate_negative_sale, Toast.LENGTH_LONG).show();
                    return;
                }
                quantity.setText(value + "");
            }
        });

        eBookName = (EditText) findViewById(R.id.book_edit_text_name);
        eBookPrice = (EditText) findViewById(R.id.book_edit_text_price);
        eQuantity = (EditText) findViewById(R.id.books_quantity);
        eSupplierName = (EditText) findViewById(R.id.supplier_edit_text_name);
        eSupplierEmail = (EditText) findViewById(R.id.supplier_edit_text_email);
        eSupplierPhone = (EditText) findViewById(R.id.supplier_edit_text__phone);

        eBookName.setOnTouchListener(touchListener);
        eBookPrice.setOnTouchListener(touchListener);
        eQuantity.setOnTouchListener(touchListener);
        eSupplierName.setOnTouchListener(touchListener);
        eSupplierEmail.setOnTouchListener(touchListener);
        eSupplierPhone.setOnTouchListener(touchListener);
        plusButton.setOnTouchListener(touchListener);
        minusButton.setOnTouchListener(touchListener);

        setTitle(R.string.add_this_product);
        invalidateOptionsMenu();

    }

    private void insertData() {
        try {
            ContentValues contentValues = new ContentValues();

            String bookName = eBookName.getText().toString();
            Integer bookQuantity = Integer.valueOf(eQuantity.getText().toString());
            Double bookPrice = -1.2;
            String suppName = eSupplierName.getText().toString();
            String suppEmail = eSupplierEmail.getText().toString();
            String suppPhone = eSupplierPhone.getText().toString();

            String errorMessage = "";
            Boolean flag = false;

            if (bookName.matches("")) {
                flag = true;
                errorMessage = errorMessage + "No Null values allowed for Book Name\n";
            }

            if (!eBookPrice.getText().toString().matches("")) {
                bookPrice = Double.valueOf(eBookPrice.getText().toString());
            }

            if (bookPrice.equals(DEFAULT_PRODUCT_PRICE)) {
                flag = true;
                errorMessage = errorMessage + "No Null values allowed for Book Price\n";
            }
            if (bookQuantity == 0) {
                flag = true;
                errorMessage = errorMessage + "Quantity of Books should not be 0\n";
            }
            if (suppName.matches("")) {
                flag = true;
                errorMessage = errorMessage + "No Null values allowed for Supplier's Name\n";
            }

            if (suppEmail.matches("")) {
                flag = true;
                errorMessage = errorMessage + "No Null values allowed for Supplier's Email Address\n";
            }

            if (suppPhone.matches("")) {
                flag = true;
                errorMessage = errorMessage + "No Null values allowed for Supplier's Phone number\n";
            }
            if (flag == true) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                return;
            }

            contentValues.put(BooksEntry.COLUMN_PRODUCT_NAME, bookName);
            contentValues.put(BooksEntry.COLUMN_PRODUCT_PRICE, bookPrice);
            contentValues.put(BooksEntry.COLUMN_PRODUCT_QUANTITY, bookQuantity);
            contentValues.put(BooksEntry.COLUMN_SUPPLIER_NAME, suppName);
            contentValues.put(BooksEntry.COLUMN_SUPPLIER_EMAIL, suppEmail);
            contentValues.put(BooksEntry.COLUMN_SUPPLIER_PHONENUMBER, suppPhone);

            Uri newUri = getContentResolver().insert(BooksEntry.CONTENT_URI, contentValues);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.unsuccessful_insertion),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.successfull_insertion),
                        Toast.LENGTH_SHORT).show();
            }

            finish();
        } catch (Exception e) {
            String exception = e.toString();
            Toast.makeText(this, exception, Toast.LENGTH_LONG).show();

        }

    }

    private void discardChangesPopup(DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.quit_editing);
        builder.setPositiveButton(R.string.discard_changes, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_adding, new DialogInterface.OnClickListener() {
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
            case R.id.menu_insert_data:
                insertData();
                return true;

            case android.R.id.home:
                if (!productChanged) {
                    NavUtils.navigateUpFromSameTask(add_book.this);
                    return true;
                }
                DialogInterface.OnClickListener cancelChangesClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(add_book.this);
                            }
                        };
                discardChangesPopup(cancelChangesClickListener);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!productChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener cancelChangesClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        discardChangesPopup(cancelChangesClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu_items, menu);
        return true;
    }

}
