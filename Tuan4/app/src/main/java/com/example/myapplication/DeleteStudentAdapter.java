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

public class DeleteStudentAdapter extends ArrayAdapter<Student> {
    private List<Student> students;
    private List<Student> selectedStudents = new ArrayList<>();

    public DeleteStudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> students) {
        super(context, resource, students);
        this.students = students;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.delete_student_list_item, null);
        }

        Student student = getItem(position);

        if (student != null) {
            TextView studentIDTextView = view.findViewById(R.id.studentID);
            TextView studentNameTextView = view.findViewById(R.id.studentName);
            TextView studentYearTextView = view.findViewById(R.id.studentYear);
            CheckBox checkBox = view.findViewById(R.id.studentCheckBox);

            studentIDTextView.setText(String.valueOf(student.getStudentID()));
            studentNameTextView.setText(student.getStudentName());
            studentYearTextView.setText(student.getStudentYear());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedStudents.add(student);
                } else {
                    selectedStudents.remove(student);
                }
            });
        }

        return view;
    }

    public List<Student> getSelectedStudents() {
        return selectedStudents;
    }
}
