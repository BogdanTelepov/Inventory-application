package com.example.bogdan.activities.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bogdan.data.ItemRepository;
import com.example.bogdan.model.Item;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private ItemRepository repository;
    private LiveData<List<Item>> allNotes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allNotes = repository.getAllNotes();

    }

    public void insert(Item item) {
        repository.insert(item);
    }

    public void update(Item item) {
        repository.update(item);
    }

    public void delete(Item item) {
        repository.delete(item);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Item>> getAllNotes() {
        return allNotes;
    }
}
