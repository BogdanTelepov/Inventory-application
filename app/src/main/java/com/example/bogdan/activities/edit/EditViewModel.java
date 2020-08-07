package com.example.bogdan.activities.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bogdan.data.ItemRepository;
import com.example.bogdan.model.Item;

import java.util.List;

public class EditViewModel extends AndroidViewModel {

    private ItemRepository repository;
    LiveData<Item> item;

    public EditViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);


    }

    public void insert(Item item) {
        repository.insert(item);
    }

    public void getItem(int id) {
        item =repository.getItem(id);
    }

    public void update(Item item) {
        repository.update(item);
    }

    public void delete(Item item) {
        repository.delete(item);
    }


    public void save(byte[] imageInByte, String title, String description, double price, int quantity) {
        Item current =  new Item(imageInByte, title, description, price, quantity);

        if (this.item == null){
            insert(current);
        } else {
            Item temp = this.item.getValue();
            if (temp != null)
            current.setId(temp.getId());

            update(current);
        }

    }
}
