package com.example.bit371finaltodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements CreateDialog.ItemListener {

    RecyclerView itemList;
    TextView emptyText;
    DBHelper dbHelper;
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = findViewById(R.id.item_recycler_view);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        emptyText = findViewById(R.id.empty_text);
        dbHelper = new DBHelper(getApplicationContext(),
                DBHelper.DATABASE_NAME, null, DBHelper.VERSION);

        adapter = new ItemListAdapter(emptyText, dbHelper);

        if (adapter.items.size() > 0) {
            emptyText.setVisibility(View.INVISIBLE);
        }

        itemList.setAdapter(adapter);

        CreateDialog createDialog = new CreateDialog();

        FloatingActionButton addbtn = findViewById(R.id.add_item_button);
        addbtn.setOnClickListener(v -> createDialog.show(getSupportFragmentManager(), "Create ToDo Item"));
    }


    @Override
    public void setItem(String text, String date) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Text or Date field was empty. No changes made.", Toast.LENGTH_SHORT).show();
        } else {
            int id = dbHelper.insertItem(text, date);
            adapter.items.add(new Item(id, text, date, false));
            adapter.notifyItemInserted(adapter.items.size());
            emptyText.setVisibility(View.INVISIBLE);
        }
    }
}