package com.example.careermentor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ChatList extends AppCompatActivity {


    ListView userList;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private String username,role;
    Map<String,String> opprole;
    Spinner roles;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        BindUIElements();


        arrayAdapter = new ArrayAdapter<String>(ChatList.this, R.layout.list_white_text,list_of_rooms);

        userList.setAdapter(arrayAdapter);
        Bundle bundle= getIntent().getExtras();
        username=bundle.getString("username");
        role=bundle.getString("role");

        opprole = new HashMap<String,String>();
        opprole.put("Student","IndustryProfessional");
        opprole.put("IndustryProfessional","Student");

        roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Set<String> set = new HashSet<String>();
                root.child("Users").child("details").child(roles.getSelectedItem().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<String>();
                Log.d(TAG,snapshot.toString());
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    Map<String,String> data = (Map<String,String>)datasnapshot.getValue();
                    Log.d(TAG,datasnapshot.toString());
                    if(data!=null && !data.get("username").equals(username)) {
                        Log.d(TAG, data.get("role") + data.get("username"));
                        set.add(data.get("username") + " - " + data.get("desc"));
                    }
                }
                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                root.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent intent = new Intent(getApplicationContext(),ChatRoom.class);
                        String roomname=((TextView)view).getText().toString().split("-")[0].trim();
                        Log.d(TAG,roomname);
                        if(snapshot.child("Users").child("details").child(role).child(username).child("rooms").child(roomname).exists()){
                            intent.putExtra("roomname",roomname);
                            intent.putExtra("roomID",snapshot.child("Users").child("details").child(role).child(username).child("rooms").child(roomname).getValue(String.class));
                            intent.putExtra("username",username);

                            startActivity(intent);
                        }else{

                            Map<String, Object> map = new HashMap<String, Object>();
                            String currentTime = Calendar.getInstance().getTime().toString();

                            map.put(roomname, currentTime);
                            root.child("Users").child("details").child(role).child(username).child("rooms").updateChildren(map);
                            map.clear();
                            map.put(username, currentTime);
                            root.child("Users").child("details").child(roles.getSelectedItem().toString()).child(roomname).child("rooms").updateChildren(map);
                            intent.putExtra("roomname",roomname);
                            intent.putExtra("roomID",currentTime);
                            intent.putExtra("username",username);
                            map.clear();
                            map.put(currentTime, "");
                            root.child("Rooms").updateChildren(map);
                            startActivity(intent);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void BindUIElements() {
        userList = (ListView) findViewById(R.id.user_list);
        roles = (Spinner) findViewById(R.id.choice);
    }
}