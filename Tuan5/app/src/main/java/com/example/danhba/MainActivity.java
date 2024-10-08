package com.example.danhba;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_WRITE_CONTACTS = 100;
    private static final int REQUEST_CODE_UPDATE_CONTACT = 103;  // Thêm request code cho cập nhật danh bạ
    private List<Contact> contactList = new ArrayList<>();
    private ContactAdapter contactAdapter;
    private boolean isAsc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo adapter
        contactAdapter = new ContactAdapter(this, contactList);
        ListView listView = findViewById(R.id.contactListView);
        listView.setAdapter(contactAdapter);

        // Kiểm tra quyền đọc và ghi danh bạ
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa có quyền, yêu cầu quyền truy cập
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                    PERMISSIONS_REQUEST_READ_WRITE_CONTACTS);
        } else {
            // Nếu đã có quyền, load danh bạ
            loadContacts();
        }

        // Điều hướng tới Activity thêm danh bạ
        Button btnAddContact = findViewById(R.id.btnAddContact);
        btnAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_CONTACT);
        });

        // Điều hướng tới Activity xóa danh bạ
        Button btnDelete = findViewById(R.id.btnDeleteContact);
        btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DeleteContactActivity.class);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_CONTACT);
        });

        // Điều hướng tới Activity sửa danh bạ khi nhấn vào danh bạ
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Contact contact = contactList.get(position);
            Intent intent = new Intent(MainActivity.this, UpdateContactActivity.class);
            intent.putExtra("contactId", contact.getId());  // Truyền ID danh bạ
            intent.putExtra("contactName", contact.getName());  // Truyền tên danh bạ
            intent.putExtra("contactNumber", contact.getNumber());  // Truyền số điện thoại danh bạ
            startActivityForResult(intent, REQUEST_CODE_UPDATE_CONTACT);
        });
    }

    // Kiểm tra kết quả khi người dùng cấp quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_WRITE_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền, load danh bạ
                loadContacts();
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Permission Denied! Cannot access contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hàm load danh bạ và hiển thị trong ListView
    public void loadContacts() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            contactList.clear(); // Xóa dữ liệu cũ
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(new Contact(id, name, phoneNumber));
            }
            cursor.close();
        }

        // Sắp xếp danh sách theo tên
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                if (isAsc) {
                    return c1.getName().compareToIgnoreCase(c2.getName()); // Tăng dần
                } else {
                    return c2.getName().compareToIgnoreCase(c1.getName()); // Giảm dần
                }
            }
        });

        // Thông báo cho adapter biết rằng dữ liệu đã thay đổi
        contactAdapter.notifyDataSetChanged();
    }

    // Tạo menu tùy chọn với hai mục ASC và DESC
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // Xử lý khi một mục trong menu được chọn
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_asc) {
            isAsc = true;  // Sắp xếp theo chiều tăng dần
            loadContacts();  // Tải lại danh bạ
            return true;
        } else if (id == R.id.menu_desc) {
            isAsc = false;  // Sắp xếp theo chiều giảm dần
            loadContacts();  // Tải lại danh bạ
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Nhận kết quả từ UpdateContactActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_CONTACT && resultCode == RESULT_OK) {
            // Nếu cập nhật danh bạ thành công, load lại danh bạ
            loadContacts();
        }
    }
}
