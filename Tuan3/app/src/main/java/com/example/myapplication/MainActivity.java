package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

public class MainActivity extends AppCompatActivity {

    private Button loginbutton;
    private EditText editUs;
    private EditText editPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        List<User> userlist = new ArrayList<>();
        userlist.add(new User("Thoi", "123"));
        userlist.add(new User("Tram", "abc"));
        userlist.add(new User("Tuyen", "xyz"));
        userlist.add(new User("Huy", "135"));
        userlist.add(new User("Nguyen", "604"));


        loginbutton = (Button) findViewById(R.id.editButton);
        editUs = (EditText) findViewById(R.id.editUser);
        editPass = (EditText) findViewById(R.id.editPassword);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editUs.getText().toString();
                String pass = editPass.getText().toString();

                for (User user : userlist) {
                    if (user.getUsername().equals(name) && user.getPassword().equals(pass)){
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("password", user.getPassword());

                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
