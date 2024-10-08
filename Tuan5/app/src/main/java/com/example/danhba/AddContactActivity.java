package com.example.danhba;

import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {

    private EditText etName, etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnAdd = findViewById(R.id.btnAdd);

        // Xử lý khi nhấn nút Thêm
        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            if (!name.isEmpty() && !phoneNumber.isEmpty()) {
                addContact(name, phoneNumber);
            } else {
                Toast.makeText(this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm thêm danh bạ
    public void addContact(String name, String phoneNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);  // Thông báo thêm danh bạ thành công
            finish();  // Kết thúc Activity sau khi thêm danh bạ
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to add contact", Toast.LENGTH_SHORT).show();
        }
    }
}
