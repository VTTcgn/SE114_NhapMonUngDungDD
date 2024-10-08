package com.example.danhba;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UpdateContactActivity extends AppCompatActivity {

    private EditText etName, etPhoneNumber;
    private String contactId;  // ID của danh bạ cần chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);

        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnSave = findViewById(R.id.btnSave);

        // Nhận thông tin từ MainActivity
        contactId = getIntent().getStringExtra("contactId");
        String contactName = getIntent().getStringExtra("contactName");
        String contactNumber = getIntent().getStringExtra("contactNumber");

        // Hiển thị thông tin danh bạ trong EditText
        etName.setText(contactName);
        etPhoneNumber.setText(contactNumber);

        // Xử lý khi nhấn nút Save
        btnSave.setOnClickListener(v -> {
            String newName = etName.getText().toString();
            String newPhoneNumber = etPhoneNumber.getText().toString();
            if (!newName.isEmpty() && !newPhoneNumber.isEmpty()) {
                updateContact(contactId, newName, newPhoneNumber);
            } else {
                Toast.makeText(this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm cập nhật danh bạ
    public void updateContact(String contactId, String newName, String newPhoneNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();

        // Cập nhật tên danh bạ
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " +
                        ContactsContract.Data.MIMETYPE + "=?", new String[]{contactId,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, newName)
                .build());

        // Cập nhật số điện thoại
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.CONTACT_ID + "=? AND " +
                        ContactsContract.Data.MIMETYPE + "=?", new String[]{contactId,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber)
                .build());

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);  // Thông báo cập nhật thành công
            finish();  // Quay lại MainActivity
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show();
        }
    }
}
