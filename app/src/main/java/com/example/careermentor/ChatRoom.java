package com.example.careermentor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    Button send_btn;
    EditText input_txt;
    TextView chat;

    private String user_name,room_name,room_ID;
    private DatabaseReference root ;
    private String temp_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        BindUIElements();

        user_name = getIntent().getExtras().get("username").toString();
        room_name = getIntent().getExtras().get("roomname").toString();
        room_ID = getIntent().getExtras().get("roomID").toString();
        setTitle(" Room - " + room_name);
        root = FirebaseDatabase.getInstance().getReference().child("Rooms").child(room_ID);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", user_name);
                map2.put("msg", input_txt.getText().toString());

                message_root.updateChildren(map2);
                input_txt.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                append_chat_conversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void BindUIElements() {
        send_btn = (Button) findViewById(R.id.btn_send);
        input_txt = (EditText) findViewById(R.id.input_txt);
        chat = (TextView) findViewById(R.id.chat_txt);
    }

    private String chat_msg,chat_user_name;

        private void append_chat_conversation(DataSnapshot dataSnapshot) {

            Iterator i = dataSnapshot.getChildren().iterator();

            while (i.hasNext()){
                chat_msg = (String) ((DataSnapshot)i.next()).getValue();
                chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
                chat.append(chat_user_name +" : "+chat_msg +" \n\n");

            }
        }


}