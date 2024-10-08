package com.example.danhba;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeleteContactActivity extends AppCompatActivity {

    private List<Contact> contactList = new ArrayList<>();
    private DeleteContactAdapter deleteContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_contact);

        ListView listView = findViewById(R.id.contactListView);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Khởi tạo adapter và gán cho ListView
        deleteContactAdapter = new DeleteContactAdapter(this, contactList);
        listView.setAdapter(deleteContactAdapter);

        // Load danh bạ
        loadContacts();

        // Xử lý sự kiện khi nhấn nút Delete
        btnDelete.setOnClickListener(v -> {
            Set<Integer> selectedContacts = deleteContactAdapter.getSelectedContacts();
            if (selectedContacts.isEmpty()) {
                Toast.makeText(this, "No contacts selected", Toast.LENGTH_SHORT).show();
            } else {
                deleteSelectedContacts(selectedContacts);
            }
        });
    }

    // Hàm load danh bạ từ điện thoại
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

        // Thông báo cho adapter biết rằng dữ liệu đã thay đổi
        deleteContactAdapter.notifyDataSetChanged();
    }

    // Hàm xóa các danh bạ đã chọn
    public void deleteSelectedContacts(Set<Integer> selectedContacts) {
        ContentResolver contentResolver = getContentResolver();

        for (Integer position : selectedContacts) {
            Contact contact = contactList.get(position);

            // Xóa danh bạ dựa trên ID của danh bạ
            long contactId = Long.parseLong(contact.getId());
            contentResolver.delete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, contactId),
                    null, null);
        }

        Toast.makeText(this, "Contacts Deleted", Toast.LENGTH_SHORT).show();

        // Sau khi xóa, thiết lập kết quả và quay về MainActivity
        setResult(RESULT_OK);  // Thông báo rằng xóa danh bạ thành công
        finish();  // Quay lại MainActivity
    }
}
