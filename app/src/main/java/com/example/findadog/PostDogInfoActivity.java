package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.app.ProgressDialog;
import android.widget.Toast;

public class PostDogInfoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txtBreed, txtGander, txtPrice, txtAddress;
    Button btnNumberInfo, btnChatInfo;
    TextView txtDescription;
    ImageSlider imageSlider;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myPosts;
    String userPhoneNumber;
    String userPostName;
    PostADog postADog;
    FirebaseAuth mAuth;
    User user;
    int postId;
    private final int SHOW_IMAGE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.carousel_info_images);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        myPosts = database.getReference("PostDog");
        user = new User();
        readFromDB();
        Intent i = getIntent();
        postADog = (PostADog) i.getSerializableExtra("postADog");
        postId = (Integer) i.getSerializableExtra("postId");
        initViews();
        initButtons();
        SetDogInfo(postADog);

    }

    private void SetDogInfo(PostADog postADog) {
        List<SlideModel> slideModels = new ArrayList<>();
        for (int k = 0; k < postADog.getUriImage().size() && k < SHOW_IMAGE; k++) {
            slideModels.add(new SlideModel(postADog.getUriImage().get(k), "", ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        txtAddress.setText("עיר: " + postADog.getAddress());
        txtBreed.setText("גזע: " + postADog.getBreedType());
        txtGander.setText("מין: " + postADog.getGender());
        Double price = postADog.getPrice();
        if (price > 0)
            txtPrice.setText("מחיר לאימוץ: " + price.toString());
        else
            txtPrice.setText("למסירה");
             txtDescription.setText(postADog.getDescription());
    }




    private void initViews() {
        imageSlider = findViewById(R.id.slider);
        txtBreed = findViewById(R.id.txtBreed);
        txtGander = findViewById(R.id.txtGander);
        txtPrice = findViewById(R.id.txtPrice);
        txtAddress = findViewById(R.id.txtAddress);
        btnNumberInfo = findViewById(R.id.btnNumberInfo);
        btnChatInfo = findViewById(R.id.btnChatInfo);
        txtDescription = findViewById(R.id.txtDescription);
        imageSlider = findViewById(R.id.slider);
    }

    private void initButtons() {

        btnChatInfo.setOnClickListener(this);
        btnNumberInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNumberInfo:
                ShowDialogPhoneNumber();
          /*      Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+userPhoneNumber));
                startActivity(callIntent);*/
                break;
            case R.id.btnChatInfo:
                ShowDialogChat();

                break;
        }
    }


    private void ShowDialogChat() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("הודעות");
        alert.setMessage(" האם לשלוח הודעה ב-whatsApp");
        alert.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("smsto:" + postADog.getPhone());
                Intent k = new Intent(Intent.ACTION_SENDTO, uri);
                k.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(k, ""));
            }
        });
        alert.setNegativeButton("לא", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.create().show();
    }
    private void ShowDialogPhoneNumber() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("האם להתקשר עכשו? ");
        alert.setMessage("מספר טלפון: " + postADog.getPhone());
        alert.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + postADog.getPhone()));
                startActivity(callIntent);
            }
        });
        alert.setNegativeButton("לא", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.create().show();
    }
    private void readFromDB() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    User temp = dp.getValue(User.class);
                    if (mAuth.getUid().equals(temp.getUserId())) {
                        user = temp;

                            //userPhoneNumber = user.getUserPhone().toString();
                            userPostName = user.getUserName();

                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}