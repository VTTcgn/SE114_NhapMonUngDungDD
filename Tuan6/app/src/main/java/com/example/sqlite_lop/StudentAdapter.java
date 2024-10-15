package com.example.sqlite_lop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class StudentAdapter extends ArrayAdapter<StudentItem> {
    private Context context;
    private ArrayList<StudentItem> studentList;
    private HashSet<Integer> selectedStudents; // Lưu các ID của sinh viên được chọn
    private boolean showCheckbox = false; // Biến kiểm soát hiển thị checkbox

    public StudentAdapter(Context context, ArrayList<StudentItem> studentList) {
        super(context, 0, studentList);
        this.context = context;
        this.studentList = studentList;
        this.selectedStudents = new HashSet<>();
    }

    // Cập nhật chế độ hiển thị checkbox
    public void setShowCheckbox(boolean showCheckbox) {
        this.showCheckbox = showCheckbox;
        notifyDataSetChanged(); // Cập nhật giao diện khi thay đổi chế độ hiển thị
    }

    public HashSet<Integer> getSelectedStudents() {
        return selectedStudents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentItem student = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        }

        CheckBox studentCheckBox = convertView.findViewById(R.id.student_checkbox);
        TextView nameTextView = convertView.findViewById(R.id.item_student_name);
        TextView dobTextView = convertView.findViewById(R.id.item_student_dob);

        // Hiển thị thông tin sinh viên
        nameTextView.setText(student.getName());
        dobTextView.setText(student.getDob());

        // Kiểm tra và hiển thị checkbox chỉ khi showCheckbox = true
        if (showCheckbox) {
            studentCheckBox.setVisibility(View.VISIBLE);
            studentCheckBox.setOnCheckedChangeListener(null); // Ngăn chặn việc set lại listener không mong muốn
            studentCheckBox.setChecked(selectedStudents.contains(student.getId()));

            studentCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedStudents.add(student.getId());
                } else {
                    selectedStudents.remove(student.getId());
                }
            });
        } else {
            studentCheckBox.setVisibility(View.GONE); // Ẩn checkbox nếu không cần hiển thị
        }

        return convertView;
    }
}
