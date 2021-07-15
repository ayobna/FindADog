package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CardListDogActivity extends AppCompatActivity {
ListView listView;
TextView txtUserName;
ArrayList<DogInfo>  dogList;
FirebaseAuth mAuth;
FirebaseDatabase database;
DatabaseReference myRef;
DatabaseReference userRef;
DogInfo dogInfo;
DogInfoAdapter dogInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_liste_dog);
        listView=findViewById(R.id.listView);
        dogList=new ArrayList<DogInfo>();
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("DogsInfo");
        userRef=database.getReference("User");
        txtUserName=findViewById(R.id.txtUserName);
        readFromDB();

        //when the user click on an item in the list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dogInfo  = dogList.get(i);
                startActivity(new Intent(CardListDogActivity.this,DogInfoActivity.class).putExtra("DogInfo",dogInfo));
            }
        });

//the bar in the bottom of the screen
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        Toast.makeText(CardListDogActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_Post:
                        startActivity(new Intent(CardListDogActivity.this,PostDogListActivity.class));
                        break;
                    case R.id.action_Post_Add:
                        startActivity(new Intent(CardListDogActivity.this,PostDogActivity.class));
                        break;
                    case R.id.action_Profile:
                        startActivity(new Intent(CardListDogActivity.this,ProfileActivity.class));
                        break;
                }
                return true;
            }
        });
    }


//a function that takes the information about the dog types from the firebase so that we can show it in a list
    private  void  readFromDB(){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //search the user that is log in so we can show its name in the head line
                for (DataSnapshot dp:snapshot.getChildren()){
                    User temp=dp.getValue(User.class);
                    if (temp.getUserId().equals(mAuth.getUid())){
                        txtUserName.setText("שלום "+temp.getUserName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
       // a function that put the information from the firebase into a list we created
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dogList.clear();
                for (DataSnapshot dp:snapshot.getChildren()){
                    DogInfo temp=dp.getValue(DogInfo.class);
                    ArrayList<String> uriImage=  temp.getUriImage();
                    dogList.add(temp);
                }
                dogInfoAdapter =new DogInfoAdapter(CardListDogActivity.this,dogList);
                listView.setAdapter(dogInfoAdapter); //set the list with the info to the adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

