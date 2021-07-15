package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPostActivity extends AppCompatActivity   {
    ListView listView;
    UserPostAdapter userPostAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    PostADog postADog;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ArrayList<PostADog> postADogList;
    ImageView btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
        listView = findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("PostDog");
        postADogList = new ArrayList<>();
        btnRemove=findViewById(R.id.btnRemove);

        readFromDB();
    }





    private void removeFromDb() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postADogList.clear();
                for (DataSnapshot dp : snapshot.getChildren()) {
                    PostADog temp = dp.getValue(PostADog.class);

                    if (postADog.equals(temp)) {
                        dp.getRef().removeValue();
                    }
                }
                userPostAdapter = new UserPostAdapter(UserPostActivity.this, postADogList);
                listView.setAdapter(userPostAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void readFromDB() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postADogList.clear();
                for (DataSnapshot dp : snapshot.getChildren()) {
                    PostADog temp = dp.getValue(PostADog.class);

                    if (mAuth.getUid().equals(temp.getUserPostId())) {
                        postADogList.add(temp);
                    }
                }
                userPostAdapter = new UserPostAdapter(UserPostActivity.this, postADogList);
                listView.setAdapter(userPostAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
