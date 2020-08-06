package com.example.bogdan.activities;

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
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bogdan.R;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.bogdan.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.bogdan.EXTRA_DESCRIPTION";
    public static final String EXTRA_QUANTITY = "com.example.bogdan.EXTRA_QUANTITY";
    public static final String EXTRA_PRICE = "com.example.bogdan.EXTRA_PRICE";
    public static final String EXTRA_IMAGE = "com.example.bogdan.EXTRA_IMAGE";
    public static final String EXTRA_ID = "com.example.bogdan.EXTRA_ID";


    final int CAMERA_REQUEST = 51;


    private EditText editText_title;
    private EditText editText_description;
    private NumberPicker numberPicker_quantity;
    private EditText editText_price;
    private Button button_takePhoto;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        editText_title = findViewById(R.id.edit_text_title);
        editText_description = findViewById(R.id.edit_text_description);
        editText_price = findViewById(R.id.edit_text_price);
        button_takePhoto = findViewById(R.id.button_takePhoto);

        numberPicker_quantity = findViewById(R.id.number_picker_quantity);
        imageView = findViewById(R.id.image_view_takePhoto);
        numberPicker_quantity.setMinValue(1);
        numberPicker_quantity.setMaxValue(100);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
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
        String title = "";
        String description = "";
        double price = 0;
        if (editText_title.getEditableText().toString().length() == 0 || editText_description.getEditableText().toString().length() == 0) {
            Toast.makeText(this, "Please input all fields", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (editText_price.getEditableText().toString().length() == 0) {
            Toast.makeText(this, "Please input price", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        title = editText_title.getText().toString();
        description = editText_description.getText().toString();
        price = Double.parseDouble(editText_price.getText().toString());
        int quantity = numberPicker_quantity.getValue();


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
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
        }

    }
}