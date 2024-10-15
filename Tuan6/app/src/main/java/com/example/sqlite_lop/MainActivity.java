package com.example.sqlite_lop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ClassItem> classList;
    private ClassAdapter adapter;
    private ListView classListView;
    private Button deleteButton;  // Nút Xóa lớp
    private HashSet<String> selectedClasses;  // Các lớp được chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Liên kết các view
        classListView = findViewById(R.id.class_list_view);
        deleteButton = findViewById(R.id.btn_delete_classes); // Nút Xóa

        // Tạo danh sách lớp
        classList = new ArrayList<>();
        selectedClasses = new HashSet<>();

        // Tải dữ liệu lớp từ database
        loadClassesFromDatabase();

        // Khởi tạo adapter
        adapter = new ClassAdapter(this, classList, selectedClasses);
        classListView.setAdapter(adapter);

        // Xử lý sự kiện nút Xóa
        deleteButton.setOnClickListener(v -> {
            if (!selectedClasses.isEmpty()) {
                ClassRepo classRepo = new ClassRepo(this);

                // Xóa từng lớp đã chọn
                for (String classId : selectedClasses) {
                    classRepo.deleteClass(classId);
                }

                // Cập nhật lại danh sách lớp sau khi xóa
                loadClassesFromDatabase();
                adapter.setShowCheckbox(false); // Ẩn checkbox sau khi xóa
                adapter.notifyDataSetChanged();

                // Ẩn nút Xóa sau khi hoàn thành
                findViewById(R.id.delete_class_button_container).setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Không có lớp nào được chọn", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện click vào một lớp học trong danh sách
        classListView.setOnItemClickListener((parent, view, position, id) -> {
            ClassItem selectedClass = classList.get(position);

            // Tạo Intent để chuyển sang ClassDetailActivity
            Intent intent = new Intent(MainActivity.this, ClassDetailActivity.class);

            // Truyền dữ liệu lớp học được chọn qua Intent
            intent.putExtra("classId", selectedClass.getClassId());
            intent.putExtra("className", selectedClass.getClassName());

            // Khởi động Activity để hiển thị chi tiết lớp học và danh sách sinh viên
            startActivity(intent);
        });
    }

    // Phương thức để tải danh sách lớp từ database
    private void loadClassesFromDatabase() {
        ClassRepo classRepo = new ClassRepo(this);
        classList.clear();
        classList.addAll(classRepo.loadAllClasses());

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_class) {
            showAddClassDialog();  // Hiển thị dialog thêm lớp
            return true;
        } else if (id == R.id.delete_class) {
            adapter.setShowCheckbox(true); // Hiển thị checkbox khi xóa lớp
            findViewById(R.id.delete_class_button_container).setVisibility(View.VISIBLE); // Hiển thị nút Xóa
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Hiển thị Dialog thêm lớp
    private void showAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Lớp");

        // Tạo layout cho dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputClassId = new EditText(this);
        inputClassId.setHint("Mã lớp");
        layout.addView(inputClassId);

        final EditText inputClassName = new EditText(this);
        inputClassName.setHint("Tên lớp");
        layout.addView(inputClassName);

        builder.setView(layout);

        // Xử lý nút Thêm
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String classId = inputClassId.getText().toString();
            String className = inputClassName.getText().toString();

            if (!classId.isEmpty() && !className.isEmpty()) {
                // Thêm lớp vào cơ sở dữ liệu
                ClassRepo classRepo = new ClassRepo(this);
                classRepo.addClass(classId, className);

                // Tải lại danh sách lớp và cập nhật adapter
                loadClassesFromDatabase();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
