package com.example.bogdan.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.bogdan.model.Item;

import java.util.List;

public class ItemRepository {
    private ItemDAO itemDAO;
    private LiveData<List<Item>> allNotes;

    public ItemRepository(Application application) {
        ItemDatabase database = ItemDatabase.getInstance(application);
        itemDAO = database.noteDao();
        allNotes = itemDAO.getAllNotes();
    }

    public void insert(Item item) {
        new InsertNoteAsyncTask(itemDAO).execute(item);
    }

    public void update(Item item) {
        new UpdateNoteAsyncTask(itemDAO).execute(item);
    }

    public void delete(Item item) {
        new DeleteNoteAsyncTask(itemDAO).execute(item);
    }

    public void deleteAllNotes() {
        new DeleteAllNotesNoteAsyncTask(itemDAO).execute();
    }

    public LiveData<List<Item>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private InsertNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.insert(items[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private UpdateNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.update(items[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDAO itemDAO;

        private DeleteNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDAO.delete(items[0]);
            return null;
        }
    }

    private static class DeleteAllNotesNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private ItemDAO itemDAO;

        private DeleteAllNotesNoteAsyncTask(ItemDAO itemDAO) {
            this.itemDAO = itemDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDAO.deleteAllNotes();
            return null;
        }
    }

}
