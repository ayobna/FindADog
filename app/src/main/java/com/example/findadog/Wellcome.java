package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.os.IResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Wellcome extends AppCompatActivity implements View.OnClickListener{

    Button btnProfile,btnWeedList,btnSupplier,btnAbout,btnSignout,btnPostADog;
    FirebaseAuth mAuth;
    TextView welcomeMessage;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference myRef;
    User user;
    ArrayList<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        mAuth = FirebaseAuth.getInstance();
        userList= new ArrayList<>();
        initViews();
        initButtons();
        welcomeMessage.setText("Wellcome "+ mAuth.getCurrentUser().getEmail());
     //   AddToDBGoogleUser();





    }

    private void AddToDBGoogleUser() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount!=null) {
        User user = new User(signInAccount.getDisplayName(), signInAccount.getId(),signInAccount.getPhotoUrl().toString());
           myRef=database.getReference("User").child(mAuth.getUid());
           myRef.setValue(user);
        }
   }
    private  void  readFromDB(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dp:snapshot.getChildren()){
                    User temp=dp.getValue(User.class);
                    if (mAuth.getUid().equals(temp.getUserId())){
                        user=temp;
//                       if (temp.getUserPhone()==null)
//                           Toast.makeText(getApplicationContext(),"אפשר לעדכן מספר טלפון דרך הפרופיל",Toast.LENGTH_LONG).show();
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        mAuth.signOut();
        super.onBackPressed();

    }
    private  void  initButtons(){
        btnProfile.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
        btnWeedList.setOnClickListener(this);
        btnSupplier.setOnClickListener(this);
        btnSignout.setOnClickListener(this);
        btnPostADog.setOnClickListener(this);
    }
    private void initViews() {
        btnProfile=findViewById(R.id.btnPtofile);
        btnSupplier=findViewById(R.id.btnSupplire);
        btnWeedList=findViewById(R.id.btnWeedList);
        btnAbout=findViewById(R.id.btnAbout);
        welcomeMessage=findViewById(R.id.welcomeMessage);
        btnSignout=findViewById(R.id.btnSignout);
        btnPostADog=findViewById(R.id.btnPostADog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPtofile:
                startActivity(new Intent(Wellcome.this,ProfileActivity.class));
                break;
            case R.id.btnWeedList:
                startActivity(new Intent(Wellcome.this,PostDogListActivity.class));
                break;
            case R.id.btnSupplire:
              //  startActivity(new Intent(Wellcome.this,HomeActivity.class));
                break;
            case R.id.btnAbout:
                startActivity(new Intent(Wellcome.this,CardListDogActivity.class));
                break;
            case R.id.btnPostADog:
                startActivity(new Intent(Wellcome.this,PostDogActivity.class));
                break;
            case R.id.btnSignout:
                FirebaseAuth.getInstance().signOut();
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();

                AccessToken.setCurrentAccessToken(null);
                signOut();
                startActivity(new Intent(Wellcome.this,MainActivity.class));
                break;

        }

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