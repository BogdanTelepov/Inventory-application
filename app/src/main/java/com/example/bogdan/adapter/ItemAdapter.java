package com.example.bogdan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bogdan.R;
import com.example.bogdan.model.Item;

public class ItemAdapter extends ListAdapter<Item, ItemAdapter.NoteHolder> {
    private OnItemClickListener listener;

    public ItemAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getQuantity() == newItem.getQuantity();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Item currentItem = getItem(position);
        holder.textView_title.setText(currentItem.getTitle());
        holder.textView_description.setText(currentItem.getDescription());
        holder.textView_quantity.setText(String.valueOf(currentItem.getQuantity()));
        holder.textView_price.setText(String.valueOf(currentItem.getPrice()));
        Glide.with(holder.imageView_productImage.getContext())
                .load(currentItem.getImage())
                .into(holder.imageView_productImage);
    }

    public Item getNoteAt(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textView_title;
        private TextView textView_description;
        private TextView textView_quantity;
        private TextView textView_price;
        private ImageView imageView_productImage;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            textView_title = itemView.findViewById(R.id.text_view_title);
            textView_description = itemView.findViewById(R.id.text_view_description);
            textView_quantity = itemView.findViewById(R.id.text_view_quantity);
            textView_price = itemView.findViewById(R.id.text_view_price);
            imageView_productImage = itemView.findViewById(R.id.image_view_product);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
