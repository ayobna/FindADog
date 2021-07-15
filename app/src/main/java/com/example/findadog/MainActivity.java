package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.AccessToken;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userEmail,userPass;
    Button btnLogin;
    FirebaseAuth mAuth;
    TextView btnSignUp,btnForgotPass;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser user;
   private final static    int   RC_SIGN_IN=123;
//    private CallbackManager callbackManager;
   // LoginButton loginButton;
    SignInButton btnGoogleLogIn;
    boolean check =true;
  //  private static final String Tag = "FacebookAuthentication";

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(MainActivity.this,CardListDogActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("User");
      //  FacebookSdk.sdkInitialize(getApplicationContext());

        initViews();
        initButtons();
        CreateRequest();
    }
    private  void  initButtons(){
        btnLogin.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
         btnGoogleLogIn.setOnClickListener(this);
      //  loginButton.setOnClickListener(this);
    }
    private void initViews() {
        userEmail =findViewById(R.id.userEmail);
        userPass=findViewById(R.id.userPass);
        btnLogin =findViewById(R.id.btnLogin);
        btnSignUp =findViewById(R.id.btnSignUp);
        btnForgotPass=findViewById(R.id.btnForgotPass);
        //loginButton = (LoginButton) findViewById(R.id.login_button);
        btnGoogleLogIn=(SignInButton)findViewById(R.id.btnGoogleLogIn);
    }
 //   private void handleFaeBookToken(AccessToken accessToken) {
//
//        Log.d(Tag,"handleFaeBookToken"+accessToken);
//        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//                         //   startActivity(new Intent(MainActivity.this,Wellcome.class));
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(MainActivity.this,"Sorry",Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//  private  void  LoginFaceBook(){
//    callbackManager = CallbackManager.Factory.create();
//
//    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            Log.d(Tag,"onSuccess"+loginResult);
//            handleFaeBookToken(loginResult.getAccessToken());
//        }
//
//
//
//        @Override
//        public void onCancel() {
//
//        }
//
//        @Override
//        public void onError(FacebookException error) {
//
//        }
//    });
//
//}
    private void CreateRequest() {

       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
       .requestEmail()
         .build();

       mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this,"לא נבחר חשבון",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                             user = mAuth.getCurrentUser();


                           ReadFromDB(mAuth.getUid());




                            startActivity(new Intent(MainActivity.this,CardListDogActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"Sorry",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void ReadFromDB(String uid) {

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dp:snapshot.getChildren()){
                        User temp=dp.getValue(User.class);

                        if (temp.getUserId().equals(uid)){
                            check=false;
                            break;
                        }
                    }
                    if (check){
                        User userSet=new User(user.getDisplayName(),mAuth.getUid(),user.getPhotoUrl().toString());
                        myRef = database.getReference("User").child(mAuth.getUid());
                        myRef.setValue(userSet);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }


    private void signInUser() {

        if(userEmail.getText().toString().equals("")||userPass.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "חובה למלות את כל השדות", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this,CardListDogActivity.class));
                        }
                        else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                break;
            case R.id.btnForgotPass:
                startActivity(new Intent(MainActivity.this,ForgotPassActivity.class));
                break;
            case R.id.btnLogin:
                signInUser();
                break;
            case R.id.btnGoogleLogIn:
                signIn();
//            case R.id.login_button:
//                //LoginFaceBook();
//                break;

        }
    }
}