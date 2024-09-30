package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddStudentActivity extends AppCompatActivity {

    private EditText editStudentID, editStudentName, editStudentYear;
    private Button buttonAddStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);

        editStudentID = findViewById(R.id.editStudentID);
        editStudentName = findViewById(R.id.editStudentName);
        editStudentYear = findViewById(R.id.editStudentYear);
        buttonAddStudent = findViewById(R.id.buttonAddStudent);

        buttonAddStudent.setOnClickListener(v -> {
            String studentID = editStudentID.getText().toString();
            String studentName = editStudentName.getText().toString();
            String studentYear = editStudentYear.getText().toString();

            if (!studentID.isEmpty() && !studentName.isEmpty() && !studentYear.isEmpty()) {
                Student newStudent = new Student(Integer.parseInt(studentID), studentName, studentYear);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newStudent", newStudent);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(AddStudentActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}