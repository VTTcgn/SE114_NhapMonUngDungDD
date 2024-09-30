package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class DeleteStudentActivity extends AppCompatActivity {

    private ListView deleteStudentListView;
    private Button buttonDeleteSelected;
    private DeleteStudentAdapter deleteStudentAdapter; // Sử dụng DeleteStudentAdapter ở đây
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_student);

        deleteStudentListView = findViewById(R.id.deleteStudentListView);
        buttonDeleteSelected = findViewById(R.id.buttonDeleteSelected);

        Intent intent = getIntent();
        studentList = (List<Student>) intent.getSerializableExtra("studentList");

        // Sử dụng DeleteStudentAdapter thay vì DeleteClassAdapter
        deleteStudentAdapter = new DeleteStudentAdapter(this, R.layout.delete_student_list_item, studentList);
        deleteStudentListView.setAdapter(deleteStudentAdapter);

        buttonDeleteSelected.setOnClickListener(v -> {
            List<Student> studentsToDelete = deleteStudentAdapter.getSelectedStudents();
            studentList.removeAll(studentsToDelete);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedStudentList", (ArrayList<Student>) studentList);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}