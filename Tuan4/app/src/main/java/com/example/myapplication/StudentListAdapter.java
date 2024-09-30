package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class StudentListAdapter extends ArrayAdapter<Student> {
    private int resource;

    public StudentListAdapter(@NonNull Context context, int resource, @NonNull List<Student> students) {
        super(context, resource, students);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resource, null);
        }

        Student student = getItem(position);

        if (student != null) {
            TextView studentIDTextView = view.findViewById(R.id.studentID);
            TextView studentNameTextView = view.findViewById(R.id.studentName);
            TextView studentYearTextView = view.findViewById(R.id.studentYear);

            studentIDTextView.setText(String.valueOf(student.getStudentID()));
            studentNameTextView.setText(student.getStudentName());
            studentYearTextView.setText(student.getStudentYear());
        }

        return view;
    }
}
