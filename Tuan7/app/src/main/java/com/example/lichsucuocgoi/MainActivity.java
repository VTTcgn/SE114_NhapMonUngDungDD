package com.example.lichsucuocgoi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 1;
    ListView callLogListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callLogListView = findViewById(R.id.callLogListView);

        // Kiểm tra quyền truy cập
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_PERMISSIONS);
        } else {
            loadCallLog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCallLog();
            }
        }
    }

    private void loadCallLog() {
        List<CallLogItem> callLogList = new ArrayList<>();

        // Các cột cần lấy từ nhật ký cuộc gọi
        String[] projection = {
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE
        };

        // Truy vấn nhật ký cuộc gọi
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                String date = DateFormat.format("EEE, dd-MM-yy hh:mm", dateMillis).toString();
                int callType = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));

                int iconResId = 0;
                switch (callType) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        iconResId = R.drawable.outgoing_call_icon;
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        iconResId = R.drawable.incoming_call_icon;
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        iconResId = R.drawable.missed_call_icon;
                        break;
                }

                // Lưu thông tin cuộc gọi vào danh sách
                callLogList.add(new CallLogItem(number, date, iconResId));

            } while (cursor.moveToNext());

            cursor.close();
        }

        // Tạo và gán adapter tuỳ chỉnh
        CallLogAdapter adapter = new CallLogAdapter(this, callLogList);
        callLogListView.setAdapter(adapter);
    }

    // Lớp adapter tùy chỉnh cho ListView
    class CallLogAdapter extends android.widget.BaseAdapter {
        private List<CallLogItem> data;
        private LayoutInflater inflater;

        public CallLogAdapter(MainActivity context, List<CallLogItem> data) {
            this.data = data;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.call_log_item, parent, false);
            }

            ImageView callTypeIcon = convertView.findViewById(R.id.callTypeIcon);
            TextView phoneNumber = convertView.findViewById(R.id.phoneNumber);
            TextView callDateTime = convertView.findViewById(R.id.callDateTime);

            CallLogItem currentItem = data.get(position);

            callTypeIcon.setImageResource(currentItem.getIconResId());
            phoneNumber.setText(currentItem.getPhoneNumber());
            callDateTime.setText(currentItem.getCallDateTime());

            return convertView;
        }
    }

    // Lớp để lưu thông tin mỗi mục trong danh sách nhật ký cuộc gọi
    class CallLogItem {
        private String phoneNumber;
        private String callDateTime;
        private int iconResId;

        public CallLogItem(String phoneNumber, String callDateTime, int iconResId) {
            this.phoneNumber = phoneNumber;
            this.callDateTime = callDateTime;
            this.iconResId = iconResId;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getCallDateTime() {
            return callDateTime;
        }

        public int getIconResId() {
            return iconResId;
        }
    }
}
