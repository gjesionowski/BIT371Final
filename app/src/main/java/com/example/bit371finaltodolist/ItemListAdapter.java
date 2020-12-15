package com.example.bit371finaltodolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder> {

    protected final ArrayList<Item> items; // While private this was not accessible to MainActivity instance of adapter
    private final DBHelper dbHelper;
    private final TextView emptyTextView;

    public ItemListAdapter(TextView emptyTextView,
                           DBHelper dbHelper) {
        this.items = dbHelper.getItems();
        this.dbHelper = dbHelper;
        this.emptyTextView = emptyTextView;
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, null);
        return new ItemListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        Item item = items.get(position);

        holder.doneBox.setChecked(item.isDone());
        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getDate());

        // When deleteButton is clicked, checks database for an item with the same ID as the selected item in view
        // Notifies view that item has been removed
        holder.deleteButton.setOnClickListener(d -> {
            Item deleteItem = items.get(holder.getAdapterPosition());
            if (dbHelper.deleteItem(deleteItem.getId())) {
                items.remove(holder.getAdapterPosition());
                notifyItemRemoved(position);
            }
            if (items.size() == 0){
                emptyTextView.setVisibility(View.VISIBLE);
            }
        });

        // Takes the item at adapter's position and checks/removes check in view holder and database
        holder.doneBox.setOnCheckedChangeListener((d, checked) -> {
            Item doneItem = items.get(holder.getAdapterPosition());
            dbHelper.setDone(doneItem.getId(), checked);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemListViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        CheckBox doneBox;
        ImageButton deleteButton;

        public ItemListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            doneBox = itemView.findViewById(R.id.checkBox);
            dateTextView = itemView.findViewById(R.id.item_date);
            deleteButton = itemView.findViewById(R.id.imageButton);
        }
    }
}
