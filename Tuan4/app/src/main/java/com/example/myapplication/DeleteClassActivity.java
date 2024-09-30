package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class DeleteClassActivity extends AppCompatActivity {
    private ListView deleteClassListView;
    private Button buttonDeleteSelected;
    private DeleteClassAdapter deleteClassAdapter;
    private List<Lop> classList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_class);

        deleteClassListView = findViewById(R.id.deleteClassListView);
        buttonDeleteSelected = findViewById(R.id.buttonDeleteSelected);

        Intent intent = getIntent();
        classList = (List<Lop>) intent.getSerializableExtra("classList");

        deleteClassAdapter = new DeleteClassAdapter(this, R.layout.delete_class_list_item, classList);
        deleteClassListView.setAdapter(deleteClassAdapter);

        buttonDeleteSelected.setOnClickListener(v -> {
            List<Lop> classesToDelete = deleteClassAdapter.getSelectedClasses();
            classList.removeAll(classesToDelete);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedClassList", (ArrayList<Lop>) classList);
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
