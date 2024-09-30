package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeleteClassAdapter extends ArrayAdapter<Lop> {
    private List<Lop> classes;
    private List<Lop> selectedClasses = new ArrayList<>();

    public DeleteClassAdapter(@NonNull Context context, int resource, @NonNull List<Lop> classes) {
        super(context, resource, classes);
        this.classes = classes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.delete_class_list_item, null);
        }

        Lop lop = getItem(position);

        if (lop != null) {
            TextView classIDTextView = view.findViewById(R.id.classID);
            TextView classNameTextView = view.findViewById(R.id.className);
            CheckBox checkBox = view.findViewById(R.id.classCheckBox);

            classIDTextView.setText(String.valueOf(lop.getID()));
            classNameTextView.setText(lop.getClassname());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedClasses.add(lop);
                } else {
                    selectedClasses.remove(lop);
                }
            });
        }

        return view;
    }

    public List<Lop> getSelectedClasses() {
        return selectedClasses;
    }
}
