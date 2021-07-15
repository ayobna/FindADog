package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


@SuppressWarnings("ALL")
public class PostDogActivity extends AppCompatActivity {
    Boolean isStart = true;
    int x = 0;
    final  static  int GET_IMAGE_FROM_GALLERY=1;
    final  static  int Max_Image=5;
EditText dogBreed,dogGender,dogBirthday,dogAddress,dogInfo,dogPhone,dogPrice;
Button btnSendPost,btnDatePicker,btnAddImage;
    DatePickerDialog datePickerDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageReference;
    DatabaseReference myUser;

    ProgressDialog progressDialog;
    ArrayList<Uri> imageList=new ArrayList<Uri>();
    Uri imageUri;
    ArrayList<String> urlList=new ArrayList<String>();


    private int upload_Count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_dog);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("PostDog");
        myUser = database.getReference("User");
        readFromDB();
        initViews();
       initDatePicker();
        btnDatePicker.setText(getTodaysDate());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_Home:
                        startActivity(new Intent(PostDogActivity.this,CardListDogActivity.class));
                        break;
                    case R.id.action_Post:
                        startActivity(new Intent(PostDogActivity.this,PostDogListActivity.class));
                        break;
                    case R.id.action_Post_Add:
                        startActivity(new Intent(PostDogActivity.this,PostDogActivity.class));
                        break;
                    case R.id.action_Profile:
                        startActivity(new Intent(PostDogActivity.this,ProfileActivity.class));
                        break;
                }
                return true;
            }
        });


        btnSendPost.setOnClickListener(new View.OnClickListener() {








            @Override
            public void onClick(View view) {
                String x=dogPhone.getText().toString();
                if (x.equals("")) {
                    Toast.makeText(getApplicationContext(), "חייב להזין מס' טלפון ליצירת קשר", Toast.LENGTH_LONG).show();
                    return;
                }
               else if (imageUri==null){
                    Toast.makeText(getApplicationContext(),"בחר תמונה",Toast.LENGTH_LONG).show();
                    return;
                }
                DialogFunc();
                uploadImage();

            }


        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(imageList.equals(new ArrayList<Uri>()))
                getImageFromGallery();
              else{
                  ShowDialogImage();
              }
            }
        });
    }
    private void ShowDialogImage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("בחר עד חמש תמנות");
        alert.setMessage("האם להוסיף עוד תמנות למה שנבחר?");
        alert.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getImageFromGallery();
            }
        });

        alert.setNegativeButton("בחר מחדש", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                imageList=new ArrayList<Uri>();
                getImageFromGallery();
            }
        });

        alert.setNeutralButton("סיום", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.create().show();
    }


    private void readFromDB() {
        myUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    User temp = dp.getValue(User.class);
//                    if (mAuth.getUid().equals(temp.getUserId())) {
//                        if (temp.getUserPhone() == null)
//                            Toast.makeText(getApplicationContext(), "אפשר לעדכן מספר טלפון דרך הפרופיל", Toast.LENGTH_LONG).show();
//                        break;
//                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void uploadImage() {
        if (imageUri!=null)

      storageReference=FirebaseStorage.getInstance().getReference().child("ImageFolder");
        progressDialog =new ProgressDialog(PostDogActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        for (upload_Count=0;upload_Count<imageList.size();upload_Count++) {
            Uri individualImage=imageList.get(upload_Count);
            final StorageReference imageName=storageReference.child("Image"+individualImage.getLastPathSegment());
            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            urlList.add(url);

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            x++;
                            if(task.isSuccessful() && (upload_Count)==x){

                               AddPostToDB();
                            }
                        }
                    });
                }
            });
        }
 }


    private void getImageFromGallery() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,GET_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case GET_IMAGE_FROM_GALLERY:
                if(resultCode ==RESULT_OK) {
                    if (data.getClipData() != null) {
                        // imgeUri = data.getData();
                        int countClipData =data.getClipData().getItemCount();
                        int currentImagesSelect = 0;
                        if (countClipData>Max_Image)
                            countClipData=Max_Image;
                        while (currentImagesSelect < countClipData) {
                            imageUri = data.getClipData().getItemAt(currentImagesSelect).getUri();
                            imageList.add((imageUri));
//                            urlList.add(imageUri.toString());
                            currentImagesSelect = currentImagesSelect + 1;
                        }

                    }else if(data.getData()!=null){
                        imageUri = data.getData();
                        imageList.add((imageUri));
                    }else
                    {
                        Toast.makeText(this, "please Select Multiple Image", Toast.LENGTH_SHORT).show();
                }


                }
                break;
        }
    }

    private String getFileExtention(Uri imgeUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap =MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imgeUri));
    }

// Data Picker
    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
    private String getTodaysDate() {
        Calendar cal =Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        month=month+1;
        int day =cal.get(Calendar.DAY_OF_MONTH);
        return  makeDateString(day,month,year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=makeDateString(day,month,year);
                btnDatePicker.setText(date);
            }
        };
        Calendar cal =Calendar.getInstance();
        int year =cal.get(Calendar.YEAR);
        int month =cal.get(Calendar.MONTH);
        int day =cal.get(Calendar.DAY_OF_MONTH);
        int style= AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog =new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
   }

    private String makeDateString(int day, int month, int year) {
        return  day+" "+getMonthFromat(month)+" "+year;
    }

    private String getMonthFromat(int month) {
    if (month==1){
            return  "JAN";
        }
        if (month==2){
            return  "FEB";
        }
        if (month==3){
            return  "MAR";
        }
        if (month==4){
            return  "ARP";
        }
        if (month==5){
            return  "MAT";
        }
        if (month==6){
            return  "JUN";
        }
        if (month==7){
            return  "JUL";
        }
        if (month==8){
            return  "AUG";
        }
        if (month==9){
            return  "SEP";
        }
        if (month==10){
            return  "OCT";
        }
        if (month==11){
            return  "NOV";
        }
        if (month==12){
            return  "DEC";
        }
        return  "JAN";
    }



    private void initViews() {
        dogBreed=findViewById(R.id.dogBreed);
        dogGender=findViewById(R.id.dogGender);
//        dogBirthday=findViewById(R.id.dogBirthday);
        dogAddress=findViewById(R.id.dogAddress);
        dogInfo=findViewById(R.id.dogInfo);
        dogPrice=findViewById(R.id.dogPrice);
        btnSendPost=findViewById(R.id.btnSendPost);
        btnDatePicker=findViewById(R.id.btnDatePicker);
        btnAddImage=findViewById(R.id.btnAddImage);
        dogPhone=findViewById(R.id.dogPhone);
    }
    private void AddPostToDB() {

        PostADog postADog =new PostADog(mAuth.getUid(),dogBreed.getText().toString(),btnDatePicker.getText().toString(),dogGender.getText().toString(),dogAddress.getText().toString()
        ,dogInfo.getText().toString(),Double.parseDouble(dogPrice.getText().toString()),dogPhone.getText().toString(),urlList);
        myRef.push().setValue(postADog);
        progressDialog.dismiss();
        startActivity(new Intent(PostDogActivity.this, CardListDogActivity.class));
    }



    private void DialogFunc() {
        progressDialog = new ProgressDialog(PostDogActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);

        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 5000);
    }

}