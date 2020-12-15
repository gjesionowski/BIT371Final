package com.example.bit371finaltodolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class CreateDialog extends DialogFragment {
    private EditText itemText;
    private CalendarView calendarView;
    private int mm, dd, yy;

    private ItemListener listener;

    public interface ItemListener{
        void setItem(String text, String date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_item_layout, container);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ItemListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.create_item_layout, null);
        itemText = view.findViewById(R.id.item_title_input);
        calendarView = view.findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendarView.getDate());
        mm = calendar.get(Calendar.MONTH) + 1;
        dd = calendar.get(Calendar.DAY_OF_MONTH);
        yy = calendar.get(Calendar.YEAR);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            dd = dayOfMonth + 1;
            yy = year;
            mm = month + 1;
        });

        builder.setTitle("New ToDo Item");
        builder.setView(view);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Add", (dialog, which) -> {
            System.out.println("Click, POSITIVE");
            String text = itemText.getText().toString();
            if(TextUtils.isEmpty(text)) {
                itemText.setError("ERROR");
            }
            listener.setItem(text, String.format("%02d/%02d/%d", mm, dd, yy));
        });
        return builder.create();
    }
}
