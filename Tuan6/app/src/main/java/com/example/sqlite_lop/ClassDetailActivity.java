package com.example.sqlite_lop;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class ClassDetailActivity extends AppCompatActivity {
    private ArrayList<StudentItem> studentList;
    private StudentAdapter adapter;
    private ListView studentListView;
    private TextView classTitleTextView, classInfoTextView;
    private Button deleteButton; // Nút Xóa
    private String classId, className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        // Nhận dữ liệu từ MainActivity
        classId = getIntent().getStringExtra("classId");
        className = getIntent().getStringExtra("className");

        // Liên kết các view
        classTitleTextView = findViewById(R.id.class_title);
        classInfoTextView = findViewById(R.id.class_info);
        studentListView = findViewById(R.id.student_list_view);
        deleteButton = findViewById(R.id.btn_delete_students); // Liên kết button "Xóa"

        // Khởi tạo danh sách sinh viên
        studentList = new ArrayList<>();

        // Thiết lập tiêu đề và thông tin lớp
        classTitleTextView.setText("Danh sách lớp " + classId);

        // Tải danh sách sinh viên từ database
        loadStudentsFromDatabase(classId);

        // Khởi tạo adapter
        adapter = new StudentAdapter(this, studentList);
        studentListView.setAdapter(adapter);

        // Cập nhật số lượng sinh viên
        classInfoTextView.setText("Số lượng sinh viên: " + studentList.size() + " - " + className);

        // Xử lý sự kiện click vào button "Xóa"
        deleteButton.setOnClickListener(v -> {
            HashSet<Integer> selectedStudents = adapter.getSelectedStudents();

            if (!selectedStudents.isEmpty()) {
                ClassRepo classRepo = new ClassRepo(this);

                // Xóa từng sinh viên đã chọn
                for (int studentId : selectedStudents) {
                    classRepo.deleteStudent(studentId);
                }

                // Tải lại danh sách sinh viên sau khi xóa
                loadStudentsFromDatabase(classId);
                adapter.setShowCheckbox(false); // Ẩn checkbox sau khi xóa
                adapter.notifyDataSetChanged(); // Cập nhật danh sách hiển thị

                // Cập nhật số lượng sinh viên trong giao diện
                classInfoTextView.setText("Số lượng sinh viên: " + studentList.size() + " - " + className);

                // Ẩn nút Xóa sau khi hoàn thành
                deleteButton.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, "Không có sinh viên nào được chọn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức lấy danh sách sinh viên từ database và cập nhật adapter
    private void loadStudentsFromDatabase(String classId) {
        ClassRepo classRepo = new ClassRepo(this);

        // Lấy danh sách sinh viên từ database
        ArrayList<StudentItem> newStudentList = classRepo.loadStudentsByClass(classId);

        // Cập nhật danh sách sinh viên trong adapter
        studentList.clear(); // Xóa danh sách cũ
        studentList.addAll(newStudentList); // Thêm sinh viên mới

        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Cập nhật giao diện
        }

        // Cập nhật số lượng sinh viên trong giao diện
        classInfoTextView.setText("Số lượng sinh viên: " + studentList.size() + " - " + className);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_student) {
            showAddStudentDialog(); // Hiển thị Dialog thêm sinh viên
            return true;
        } else if (id == R.id.delete_student) {
            adapter.setShowCheckbox(true); // Hiển thị checkbox khi xóa sinh viên
            deleteButton.setVisibility(View.VISIBLE); // Hiển thị nút Xóa
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Hiển thị Dialog thêm sinh viên (phần này giữ nguyên)
    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Sinh Viên");

        // Tạo layout cho dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputName = new EditText(this);
        inputName.setHint("Họ và tên");
        layout.addView(inputName);

        final EditText inputDob = new EditText(this);
        inputDob.setHint("Ngày sinh (dd/MM/yyyy)");
        layout.addView(inputDob);

        builder.setView(layout);

        // Xử lý nút Thêm
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = inputName.getText().toString();
            String dob = inputDob.getText().toString();

            // Thêm sinh viên vào cơ sở dữ liệu
            ClassRepo classRepo = new ClassRepo(this);
            classRepo.addStudent(name, dob, classId);

            // Tải lại danh sách sinh viên sau khi thêm
            loadStudentsFromDatabase(classId);
            adapter.notifyDataSetChanged(); // Cập nhật danh sách hiển thị
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}

