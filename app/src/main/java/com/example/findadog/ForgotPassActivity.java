package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    Button btnSendRestToEmail;
    EditText userEmail;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        btnSendRestToEmail=findViewById(R.id.btnSendRestToEmail);
        userEmail=findViewById(R.id.userEmail);
        mAuth=FirebaseAuth.getInstance();
        btnSendRestToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.sendPasswordResetEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassActivity.this,"הסיסמה נשלחה למייל",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPassActivity.this,MainActivity.class));
                        }
                        else
                            Toast.makeText(ForgotPassActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}