package com.example.careermentor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class Signup extends AppCompatActivity {
    EditText uname,pass,desc;
    Button singup_btn;
    Spinner roles;
    DatabaseReference DBRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        BindUIElements();

        DBRef = FirebaseDatabase.getInstance().getReference("Users");

        singup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount(){
        String username= uname.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String role = roles.getSelectedItem().toString().trim();
        String descript = desc.getText().toString().trim();
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (username.length() <= 0 || password.length() <= 0) {
                    Toast.makeText(Signup.this, "username and password can't be empty", Toast.LENGTH_SHORT).show();
                } else if (snapshot.child("details").child(role).child(username).exists()) {
                    Toast.makeText(Signup.this, "User already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    String specialcharactersRegex = ".*[!@#$%^&*_+].*";
                    String uppercaseRegex = ".*[A-Z].*";
                    String lowercaseRegex = ".*[a-z].*";
                    String numbersRegex = ".*[0-9].*";

                    if (!password.matches(specialcharactersRegex)) {
                        Toast.makeText(Signup.this, "enter special char", Toast.LENGTH_SHORT).show();
                    } else if (!password.matches(uppercaseRegex)) {
                        Toast.makeText(Signup.this, "enter upper char", Toast.LENGTH_SHORT).show();
                    } else if (!password.matches(lowercaseRegex)) {
                        Toast.makeText(Signup.this, "enter lower char", Toast.LENGTH_SHORT).show();
                    } else if (!password.matches(numbersRegex)) {
                        Toast.makeText(Signup.this, "enter number", Toast.LENGTH_SHORT).show();
                    } else {
                        User userdetails = new User(username, password, role, descript);

                        DBRef.child(username).setValue(userdetails);
                        Toast.makeText(Signup.this, "SignUp successfully! ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void finish(){
        Intent intent = new Intent(Signup.this,MainActivity.class);

        startActivity(intent);
    }



    private void BindUIElements(){
        uname = (EditText) findViewById(R.id.signup_username);
        pass = (EditText) findViewById(R.id.signup_password);
        singup_btn = (Button) findViewById(R.id.signup_button);
        roles = (Spinner) findViewById(R.id.signup_role);
        desc = (EditText) findViewById(R.id.signup_desc);
    }
}