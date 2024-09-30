package com.example.myapplication;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.R;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

class Lop extends Person {
    private List<Student> students;

    public Lop(int ID, String classname, List<Student> students) {
        super(ID, classname);
        this.students = students;
    }

    public String getClassname() {
        return getName();
    }

    public void setClassname(String classname) {
        setName(classname);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}

public class MainActivity extends AppCompatActivity {

    private ListView classListView;
    private ClassListAdapter adapter;
    private List<Lop> classList;
    private static final int REQUEST_CODE_ADD_CLASS = 1;
    private static final int REQUEST_CODE_DELETE_CLASS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        classListView = findViewById(R.id.classListView);

        List<Student> studentsClass1 = new ArrayList<>();
        studentsClass1.add(new Student(1, "Nguyen Van A", "2000"));
        studentsClass1.add(new Student(2, "Tran Thi B", "1999"));
        studentsClass1.add(new Student(3, "Le Van C", "2001"));

        List<Student> studentsClass2 = new ArrayList<>();
        studentsClass2.add(new Student(1, "Pham Minh D", "2000"));
        studentsClass2.add(new Student(2, "Hoang Thi E", "1998"));

        List<Student> studentsClass3 = new ArrayList<>();
        studentsClass3.add(new Student(1, "Nguyen Van F", "2002"));
        studentsClass3.add(new Student(2, "Tran Thi G", "2001"));

        classList = new ArrayList<>();
        classList.add(new Lop(1, "IT001", studentsClass1));
        classList.add(new Lop(2, "IT002", studentsClass2));
        classList.add(new Lop(3, "IT003", studentsClass3));

        adapter = new ClassListAdapter(this, R.layout.class_list_item, classList);
        classListView.setAdapter(adapter);

        classListView.setOnItemClickListener((parent, view, position, id) -> {
            Lop selectedClass = classList.get(position);

            Intent intent = new Intent(MainActivity.this, ClassDetailActivity.class);
            intent.putExtra("classID", selectedClass.getID());
            intent.putExtra("classname", selectedClass.getClassname());

            intent.putExtra("studentList", new ArrayList<>(selectedClass.getStudents()));
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, AddClassActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_CLASS);
            return true;
        } else if (id == R.id.action_delete) {
            Intent deleteIntent = new Intent(MainActivity.this, DeleteClassActivity.class);
            deleteIntent.putExtra("classList", (ArrayList<Lop>) classList);
            startActivityForResult(deleteIntent, REQUEST_CODE_DELETE_CLASS);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_CLASS && resultCode == RESULT_OK) {
            int classId = Integer.parseInt(data.getStringExtra("class_id"));
            String className = data.getStringExtra("class_name");
            List<Student> newStudentList = new ArrayList<>();
            Lop newClass = new Lop(classId, className, newStudentList);
            classList.add(newClass);
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_DELETE_CLASS && resultCode == RESULT_OK) {
            classList = (List<Lop>) data.getSerializableExtra("updatedClassList");
            adapter = new ClassListAdapter(this, R.layout.class_list_item, classList);
            classListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}


class ClassListAdapter extends ArrayAdapter<Lop>{
    int resource;
    private List<Lop> Lops;
    public ClassListAdapter(Context context, int resource, List<Lop> Lops) {
        super(context, resource, Lops);
        this.Lops = Lops;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(this.resource, null);
        }
        Lop s = getItem(position);

        if (s != null) {
            TextView idTextView = (TextView) v.findViewById(R.id.ID);
            TextView nameTextView = (TextView) v.findViewById(R.id.classname);

            if (idTextView != null)
                idTextView.setText(String.valueOf(s.getID()));
            if (nameTextView != null)
                nameTextView.setText(s.getClassname());
        }
        return v;
    }
}

