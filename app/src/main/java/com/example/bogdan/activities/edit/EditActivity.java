package com.example.bogdan.activities.edit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bogdan.R;
import com.example.bogdan.model.Item;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.bogdan.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.bogdan.EXTRA_DESCRIPTION";
    public static final String EXTRA_QUANTITY = "com.example.bogdan.EXTRA_QUANTITY";
    public static final String EXTRA_PRICE = "com.example.bogdan.EXTRA_PRICE";
    public static final String EXTRA_IMAGE = "com.example.bogdan.EXTRA_IMAGE";
    public static final String EXTRA_ID = "com.example.bogdan.EXTRA_ID";

    EditViewModel editViewModel;
    final int CAMERA_REQUEST = 51;

    private Bitmap bitmap;

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

        editViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(EditViewModel.class);

        editText_title = findViewById(R.id.edit_text_title);
        editText_description = findViewById(R.id.edit_text_description);
        editText_price = findViewById(R.id.edit_text_price);
        button_takePhoto = findViewById(R.id.button_takePhoto);

        numberPicker_quantity = findViewById(R.id.number_picker_quantity);
        imageView = findViewById(R.id.image_view_takePhoto);
        numberPicker_quantity.setMinValue(1);
        numberPicker_quantity.setMaxValue(100);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        if (getIntent().hasExtra(EXTRA_ID)) {
            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            editViewModel.getItem(id);
            editViewModel.item.observe(this, new Observer<Item>() {
                @Override
                public void onChanged(Item item) {
                    if (item == null) return;
                    editText_title.setText(item.getTitle());
                    editText_description.setText(item.getDescription());

                    Bitmap bmp = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                    imageView.setImageBitmap(bmp);
                }
            });
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
                showAlertDialogButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String title = "";
                String description = "";
                double price = 0;
                if (editText_title.getEditableText().toString().length() == 0 || editText_description.getEditableText().toString().length() == 0) {
                    Toast.makeText(EditActivity.this, "Please input all fields", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (editText_price.getEditableText().toString().length() == 0) {
                    Toast.makeText(EditActivity.this, "Please input price", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                title = editText_title.getText().toString();
                description = editText_description.getText().toString();
                price = Double.parseDouble(editText_price.getText().toString());
                int quantity = numberPicker_quantity.getValue();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                byte[] imageInByte = stream.toByteArray();

                editViewModel.save(imageInByte, title, description, price, quantity);

                finish();
            }
        });
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
                bitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(bitmap);
            }
        }

    }


    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete item");
        builder.setMessage("Would you like to delete item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(EditActivity.this, "Item delete", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}