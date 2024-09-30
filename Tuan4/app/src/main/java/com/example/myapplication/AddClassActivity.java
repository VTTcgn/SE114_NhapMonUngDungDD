package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AddClassActivity extends AppCompatActivity {
    private EditText editTextClassID, editTextClassName;
    private Button buttonAddClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_class);

        editTextClassID = findViewById(R.id.editTextClassID);
        editTextClassName = findViewById(R.id.editTextClassName);
        buttonAddClass = findViewById(R.id.buttonAddClass);

        buttonAddClass.setOnClickListener(v -> {
            String classID = editTextClassID.getText().toString();
            String className = editTextClassName.getText().toString();

            if (!classID.isEmpty() && !className.isEmpty()) {
                int classIdInt = Integer.parseInt(classID);
                Lop newClass = new Lop(classIdInt, className, new ArrayList<>());

                Intent resultIntent = new Intent();
                resultIntent.putExtra("class_id", String.valueOf(newClass.getID()));
                resultIntent.putExtra("class_name", newClass.getClassname());
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(AddClassActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}