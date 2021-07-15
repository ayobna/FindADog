package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Button btnSignOut,btnPostDogProfile,btnEditProfile;
    TextView txtUserPhone,txtEmail,txtUserName;
    CircleImageView imageProfile;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("User");
        initViews();
        readFromDB();
        initButtons();


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        startActivity(new Intent(ProfileActivity.this,CardListDogActivity.class));
                        break;
                    case R.id.action_Post:
                        startActivity(new Intent(ProfileActivity.this,PostDogListActivity.class));
                        break;
                    case R.id.action_Post_Add:
                        startActivity(new Intent(ProfileActivity.this,PostDogActivity.class));
                        break;
                    case R.id.action_Profile:
                        startActivity(new Intent(ProfileActivity.this,ProfileActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    private void initViews() {
        txtEmail = findViewById(R.id.txtEmail);
        txtUserName = findViewById(R.id.txtUserName);
        imageProfile=findViewById(R.id.imageProfile);
        btnSignOut=findViewById(R.id.btnSignOut);
        btnPostDogProfile=findViewById(R.id.btnPostDogProfile);
        btnEditProfile=findViewById(R.id.btnEditProfile);
    }


    private  void  initButtons(){
        btnSignOut.setOnClickListener(this);
        btnPostDogProfile.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);
    }


    private  void  readFromDB(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dp:snapshot.getChildren()){
                    User temp=dp.getValue(User.class);
                    if (mAuth.getUid().equals(temp.getUserId())) {
//                        if (temp.getUserPhone() != null)
//                        {
//                            txtUserPhone.setText(temp.getUserPhone());
//                    }
                        txtUserName.setText(temp.getUserName());
                        txtEmail.setText(mAuth.getCurrentUser().getEmail());
                        if (!imageProfile.equals(null))
                            Picasso.get().load(temp.getUriImage()).into(imageProfile);
                        user =temp;
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btnPostDogProfile:
                  startActivity(new Intent(ProfileActivity.this,UserPostActivity.class));
                break;
            case R.id.btnSignOut:

            //    AccessToken.setCurrentAccessToken(null);
                signOut();
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                break;
            case  R.id.btnEditProfile:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class).putExtra("User",user));
                break;
        }
    }


    public void onBackPressed() {
        mAuth.signOut();
        super.onBackPressed();

    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
}