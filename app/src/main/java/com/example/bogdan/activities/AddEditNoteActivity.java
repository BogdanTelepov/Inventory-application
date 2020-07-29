package com.example.bogdan.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.bogdan.R;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.bogdan.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.bogdan.EXTRA_DESCRIPTION";
    public static final String EXTRA_QUANTITY = "com.example.bogdan.EXTRA_QUANTITY";
    public static final String EXTRA_PRICE = "com.example.bogdan.EXTRA_PRICE";
    public static final String EXTRA_IMAGE = "com.example.bogdan.EXTRA_IMAGE";
    public static final String EXTRA_ID = "com.example.bogdan.EXTRA_ID";

    public static int CAMERA_INTENT = 51;

    private EditText editText_title;
    private EditText editText_description;
    private NumberPicker numberPicker_quantity;
    private Bitmap bitmapImage;
    private EditText editText_price;
    private Button button_takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editText_title = findViewById(R.id.edit_text_title);
        editText_description = findViewById(R.id.edit_text_description);
        editText_price = findViewById(R.id.edit_text_price);
        button_takePhoto = findViewById(R.id.button_takePhoto);
        bitmapImage = null;
        numberPicker_quantity = findViewById(R.id.number_picker_quantity);

        numberPicker_quantity.setMinValue(1);
        numberPicker_quantity.setMaxValue(100);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editText_title.setText(intent.getStringExtra(EXTRA_TITLE));
            editText_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            editText_price.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRICE, 1)));
            numberPicker_quantity.setValue(intent.getIntExtra(EXTRA_QUANTITY, 1));
        } else {
            setTitle("Add Note");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        menuInflater.inflate(R.menu.delete_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            case R.id.delete_note:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String title = editText_title.getText().toString();
        String description = editText_description.getText().toString();
        int quantity = numberPicker_quantity.getValue();
        double price = Double.parseDouble(editText_price.getText().toString());

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please input title and description", Toast.LENGTH_SHORT)
                    .show();
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_QUANTITY, quantity);
        data.putExtra(EXTRA_PRICE, price);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();


    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_INTENT);
        }
    }
}