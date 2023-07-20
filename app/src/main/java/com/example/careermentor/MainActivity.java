package com.example.careermentor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText uname,pass;
    Button login_btn,singup_btn;
    Spinner roles;
    DatabaseReference DBRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindUIElements();
        DBRef = FirebaseDatabase.getInstance().getReference("Users");
        uname.setText("");
        pass.setText("");

        singup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, Signup.class);

                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public void login(){
        String username= uname.getText().toString();
        String password = pass.getText().toString();
        String role = roles.getSelectedItem().toString();
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("details").child(role).child(username).exists()){
                    User user_data = snapshot.child("details").child(role).child(username).getValue(User.class);
                    if(user_data.getPassword().equals(password) && user_data.getRole().equals(role)){
                        Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, ChatList.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("username",username);
                        bundle.putString("role",role);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }else{
                        Toast.makeText(MainActivity.this,"Password or Role is incorrect",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,"Username doesn't exist! Signup First.",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void BindUIElements(){
        uname = (EditText) findViewById(R.id.login_username);
        pass = (EditText) findViewById(R.id.login_password);
        login_btn = (Button) findViewById(R.id.login_button);
        singup_btn = (Button) findViewById(R.id.signup_new_button);
        roles = (Spinner) findViewById(R.id.login_role);

    }


}