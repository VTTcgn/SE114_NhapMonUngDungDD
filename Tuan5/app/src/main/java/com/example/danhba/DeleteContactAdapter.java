package com.example.danhba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeleteContactAdapter extends BaseAdapter {

    private Context context;
    private List<Contact> contacts;
    private Set<Integer> selectedContacts = new HashSet<>();  // Set lưu các vị trí đã chọn

    public DeleteContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item_with_checkbox, parent, false);
        }

        Contact contact = contacts.get(position);

        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtNumber = convertView.findViewById(R.id.txtNumber);
        CheckBox checkBox = convertView.findViewById(R.id.checkBox);

        txtName.setText(contact.getName());
        txtNumber.setText(contact.getNumber());

        // Thiết lập trạng thái của checkbox dựa vào Set lưu trữ
        checkBox.setChecked(selectedContacts.contains(position));

        // Xử lý sự kiện khi người dùng nhấn vào checkbox
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedContacts.add(position);  // Nếu được chọn, thêm vào Set
            } else {
                selectedContacts.remove(position);  // Nếu bỏ chọn, xóa khỏi Set
            }
        });

        return convertView;
    }

    // Trả về danh sách các vị trí đã chọn
    public Set<Integer> getSelectedContacts() {
        return selectedContacts;
    }
}
