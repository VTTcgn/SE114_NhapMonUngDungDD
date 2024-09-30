package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ClassDetailActivity extends AppCompatActivity {
    private TextView classNameTextView, studentCountTextView;
    private ListView studentListView;
    private StudentListAdapter adapter;
    private List<Student> students;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        classNameTextView = findViewById(R.id.classNameTextView);
        studentCountTextView = findViewById(R.id.studentCountTextView);
        studentListView = findViewById(R.id.studentListView);

        String classname = getIntent().getStringExtra("classname");

        students = (ArrayList<Student>) getIntent().getSerializableExtra("studentList");

        if (students == null) {
            students = new ArrayList<>();
        }

        classNameTextView.setText("Tên lớp: " + classname);
        studentCountTextView.setText("Tổng số sinh viên: " + students.size());

        adapter = new StudentListAdapter(this, R.layout.student_list_item, students);
        studentListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.class_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add_student) {
            Intent intent = new Intent(ClassDetailActivity.this, AddStudentActivity.class);
            intent.putExtra("classname", classNameTextView.getText().toString());
            startActivityForResult(intent, 101); // Request code 101 cho chức năng thêm
            return true;
        } else if (id == R.id.action_delete_student) {
            Intent intent = new Intent(ClassDetailActivity.this, DeleteStudentActivity.class);
            intent.putExtra("classname", classNameTextView.getText().toString());
            intent.putExtra("studentList", (ArrayList<Student>) students);
            startActivityForResult(intent, 102); // Request code 102 cho chức năng xóa
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) { // Thêm sinh viên
            Student newStudent = (Student) data.getSerializableExtra("newStudent");
            students.add(newStudent);
            studentCountTextView.setText("Tổng số sinh viên: " + students.size());
            adapter.notifyDataSetChanged();
        } else if (requestCode == 102 && resultCode == RESULT_OK) { // Xóa sinh viên
            students = (ArrayList<Student>) data.getSerializableExtra("updatedStudentList");
            studentCountTextView.setText("Tổng số sinh viên: " + students.size());
            adapter = new StudentListAdapter(this, R.layout.student_list_item, students);
            studentListView.setAdapter(adapter);
        }
    }
}