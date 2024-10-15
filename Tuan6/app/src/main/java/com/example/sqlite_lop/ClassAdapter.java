package com.example.sqlite_lop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class ClassAdapter extends ArrayAdapter<ClassItem> {
    private Context context;
    private ArrayList<ClassItem> classList;
    private HashSet<String> selectedClasses;  // Danh sách các lớp được chọn
    private boolean showCheckbox = false;     // Biến để kiểm tra xem có hiển thị checkbox không

    // Constructor cho adapter
    public ClassAdapter(Context context, ArrayList<ClassItem> classList, HashSet<String> selectedClasses) {
        super(context, 0, classList);
        this.context = context;
        this.classList = classList;
        this.selectedClasses = selectedClasses;
    }

    // Phương thức kích hoạt hoặc vô hiệu hóa hiển thị checkbox
    public void setShowCheckbox(boolean showCheckbox) {
        this.showCheckbox = showCheckbox;
        notifyDataSetChanged();  // Cập nhật lại giao diện
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Lấy đối tượng class hiện tại
        ClassItem classItem = getItem(position);

        // Kiểm tra nếu classItem là null
        if (classItem == null) {
            return convertView;
        }

        ViewHolder viewHolder;
        // Kiểm tra xem view có đang được tái sử dụng không, nếu không thì inflate layout và tạo ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_item, parent, false);
            viewHolder = new ViewHolder();

            // Liên kết các view trong layout với ViewHolder
            viewHolder.idTextView = convertView.findViewById(R.id.item_id);
            viewHolder.classCodeTextView = convertView.findViewById(R.id.item_class_code);
            viewHolder.classNameTextView = convertView.findViewById(R.id.item_class_name);
            viewHolder.classCheckBox = convertView.findViewById(R.id.class_checkbox);  // Liên kết checkbox

            // Lưu ViewHolder vào trong convertView để tái sử dụng
            convertView.setTag(viewHolder);
        } else {
            // Nếu view đang tái sử dụng, lấy ViewHolder đã lưu từ convertView
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Đặt dữ liệu cho từng TextView
        viewHolder.idTextView.setText(String.valueOf(position + 1)); // Số thứ tự
        viewHolder.classCodeTextView.setText(classItem.getClassId()); // Mã lớp
        viewHolder.classNameTextView.setText(classItem.getClassName()); // Tên lớp

        // Xử lý hiển thị checkbox khi xóa lớp
        if (showCheckbox) {
            viewHolder.classCheckBox.setVisibility(View.VISIBLE);
            viewHolder.classCheckBox.setChecked(selectedClasses.contains(classItem.getClassId()));

            // Xử lý chọn/bỏ chọn lớp
            viewHolder.classCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedClasses.add(classItem.getClassId());  // Thêm lớp vào danh sách chọn
                } else {
                    selectedClasses.remove(classItem.getClassId());  // Bỏ lớp khỏi danh sách chọn
                }
            });
        } else {
            viewHolder.classCheckBox.setVisibility(View.GONE);  // Ẩn checkbox khi không cần xóa lớp
        }

        // Trả về view cho hàng hiện tại trong ListView
        return convertView;
    }

    // ViewHolder để lưu trữ view con
    private static class ViewHolder {
        TextView idTextView;
        TextView classCodeTextView;
        TextView classNameTextView;
        CheckBox classCheckBox;  // Checkbox để chọn lớp
    }
}
