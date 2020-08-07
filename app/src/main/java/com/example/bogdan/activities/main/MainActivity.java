package com.example.bogdan.activities.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bogdan.R;
import com.example.bogdan.activities.edit.EditActivity;
import com.example.bogdan.adapter.ItemAdapter;
import com.example.bogdan.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ItemAdapter adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        mainViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(MainViewModel.class);


        mainViewModel.getAllNotes().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                adapter.submitList(items);


            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                intent.putExtra(EditActivity.EXTRA_ID, item.getId());
                startActivity(intent);
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
//            assert data != null;
//            String title = data.getStringExtra(EditActivity.EXTRA_TITLE);
//            String description = data.getStringExtra(EditActivity.EXTRA_DESCRIPTION);
//            int quantity = data.getIntExtra(EditActivity.EXTRA_QUANTITY, 1);
//            double price = data.getDoubleExtra(EditActivity.EXTRA_PRICE, 1);
//            byte[] image = data.getByteArrayExtra(EditActivity.EXTRA_IMAGE);
//
//            Item item = new Item(image, title, description, price, quantity);
//            mainViewModel.insert(item);
//            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT)
//                    .show();
//        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
//            assert data != null;
//            int id = data.getIntExtra(EditActivity.EXTRA_ID, -1);
//            if (id == -1) {
//                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//            String title = data.getStringExtra(EditActivity.EXTRA_TITLE);
//            String description = data.getStringExtra(EditActivity.EXTRA_DESCRIPTION);
//            int quantity = data.getIntExtra(EditActivity.EXTRA_QUANTITY, 1);
//            double price = data.getDoubleExtra(EditActivity.EXTRA_PRICE, 1);
//            byte[] image = data.getByteArrayExtra(EditActivity.EXTRA_IMAGE);
//
//
//            Item item = new Item(image, title, description, price, quantity);
//            item.setId(id);
//            mainViewModel.update(item);
//            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT)
//                    .show();
//
//
//        } else {
//            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT)
//                    .show();
//        }
//
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.delete_all_notes:
                showAlertDialogButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showAlertDialogButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all notes");
        builder.setMessage("Would you like to delete all notes?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainViewModel.deleteAllNotes();
                Toast.makeText(MainActivity.this, "All notes delete", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}