package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostDogListActivity extends AppCompatActivity {
ListView listView;
PostDogListAdapter postDogListAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    PostADog postADog;
 ProgressDialog progressDialog;
    ArrayList<PostADog> postADogList;
boolean proDialog =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_dog_list);
        listView=findViewById(R.id.listView);
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("PostDog");
        postADogList=new ArrayList<>();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        startActivity(new Intent(PostDogListActivity.this,CardListDogActivity.class));
                        break;
                    case R.id.action_Post:
                        startActivity(new Intent(PostDogListActivity.this,PostDogListActivity.class));
                        break;
                    case R.id.action_Post_Add:
                        startActivity(new Intent(PostDogListActivity.this,PostDogActivity.class));
                        break;
                    case R.id.action_Profile:
                        startActivity(new Intent(PostDogListActivity.this,ProfileActivity.class));
                        break;
                }
                return true;
            }
        });

// if(proDialog==true) {
//     progressDialog = new ProgressDialog(PostDogListActivity.this);
//     progressDialog.show();
//     progressDialog.setContentView(R.layout.progress_dialog);
//
//     progressDialog.getWindow().setBackgroundDrawableResource(
//             android.R.color.transparent
//     );
//     proDialog = false;
//
//
//     Handler handler = new Handler();
//     handler.postDelayed(new Runnable() {
//         @Override
//         public void run() {
//             progressDialog.dismiss();
//         }
//     }, 2000);
// }
        readFromDB();
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

       // Toast.makeText(getApplicationContext(),postADogList.get(i).getGender(),Toast.LENGTH_SHORT).show();

       postADog  = postADogList.get(i);
        startActivity(new Intent(PostDogListActivity.this,PostDogInfoActivity.class).
                putExtra("postADog",postADog).putExtra("postId",postADog.getIdPost()));

    }
});

    }
    private  void  readFromDB(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postADogList.clear();
                for (DataSnapshot dp:snapshot.getChildren()){
                    PostADog temp=dp.getValue(PostADog.class);
                    postADogList.add(temp);
                }
                postDogListAdapter =new PostDogListAdapter(PostDogListActivity.this,postADogList);
                listView.setAdapter(postDogListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
