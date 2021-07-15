package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText userName,userEmail,userPass,userRePass,userId;
    Button btnSignup,btnOpenCamera,btnPicImage,btnCancel;
    CircleImageView imageView;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageReference;
    Uri UriImage;
    String imagePath=null;
    final  static  int GET_IMAGE_FROM_GALLERY=1;
    private static final int CAMERA_REQUEST_CODE=101;
    // Create a storage reference from our app
    FirebaseStorage storage;
    StorageReference storageRef;
    // Create a reference to "mountains.jpg"
    StorageReference imageRef ;
    // Create a reference to 'images/mountains.jpg'
    StorageReference mountainImagesRef ;
    Bitmap imageBitmap;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
         storage = FirebaseStorage.getInstance();
         storageRef = storage.getReference();


        //if (mAuth!=null)
          myRef=database.getReference("User");
        initViews();
        initButtons();
       // btnSignUp.setOnClickListener(this);

    }
    private  void  initViews(){
        userEmail=findViewById(R.id.userEmail);
        userName=findViewById(R.id.userName);
        userPass=findViewById(R.id.userPass);
        userRePass=findViewById(R.id.userRePass);
        btnSignup = findViewById(R.id.btnSignUp);
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        imageView = findViewById(R.id.imageView);
        btnPicImage=findViewById(R.id.btnPicImage);
        btnCancel=findViewById(R.id.btnCancel);
    }
    private  void  initButtons(){
        btnSignup.setOnClickListener(this);
        btnOpenCamera.setOnClickListener(this);
        btnPicImage.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                signUpUser();
                break;
            case R.id.btnOpenCamera:
                OpenCamera();
                break;
            case R.id.btnPicImage:
                getImageFromGallery();
                break;
            case R.id.btnCancel:
                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                break;
        }
    }
    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, GET_IMAGE_FROM_GALLERY);
    }

    private void OpenCamera() {
                Intent open_Camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(open_Camera,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch(requestCode) {
                case CAMERA_REQUEST_CODE :
                        if (resultCode == RESULT_OK) {
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Bundle extras = data.getExtras();
                            imageBitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(imageBitmap);
                            UriImage=getImageUri(SignUpActivity.this,imageBitmap);
                        }
                    break;
                case GET_IMAGE_FROM_GALLERY :
                    if (resultCode == RESULT_OK) {
                        UriImage = data.getData();
                        Picasso.get().load(UriImage).into(imageView);
                    }
                    break;
            }


}
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void UploadImage() {

        progressDialog =new ProgressDialog(SignUpActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        storageReference= FirebaseStorage.getInstance().getReference().child("Users_image");
        Uri individualImage=UriImage;
        final StorageReference imageName=storageReference.child( userEmail.getText().toString());
        imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url=uri.toString();
                        imagePath =url;
                        signUp();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    private  void  signUpUser(){

        if(userPass.getText().toString().equals("")||userRePass.getText().toString().equals("")||
                userEmail.getText().toString().equals("")||userName.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "חובה למלות את כל השדות", Toast.LENGTH_LONG).show();
            return;
        }
        if (!userPass.getText().toString().equals(userRePass.getText().toString())){
            Toast.makeText(getApplicationContext(), "הסיסמאות לא תואמות", Toast.LENGTH_LONG).show();
            return;
        }
        if (UriImage !=null) {
            UploadImage();
        }else {
            signUp();
        }

    }
    private  void  signUp(){
        mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(),userPass.getText().toString())
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //register complete successfully.
                            User user=new User(userName.getText().toString(),mAuth.getUid(),imagePath);
                            myRef=database.getReference("User").child(mAuth.getUid());
                            myRef.setValue(user);
                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));

                        }else{
                            //Error
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

//    private void uploadImage() {
//        progressDialog =new ProgressDialog(SignUpActivity.this);
//        progressDialog.show();
//        progressDialog.setContentView(R.layout.progress_dialog);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] dataByte = baos.toByteArray();
//        imageRef = storageRef.child("Users_image/"+userEmail.getText().toString()+".jpg");
//        UploadTask uploadTask = imageRef.putBytes(dataByte);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//                Toast.makeText(getApplicationContext()," Handle unsuccessful uploads",Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//
//              //  imagePath=taskSnapshot.getUploadSessionUri().toString();
//            //    Toast.makeText(getApplicationContext(),imagePath,Toast.LENGTH_LONG).show();
//
//                //Toast.makeText(getApplicationContext(),"  Successful uploads",Toast.LENGTH_LONG).show();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                progressDialog.dismiss();
//                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String url=uri.toString();
//                     imagePath =url;
//                        signUp();
//                    }
//
//            });
//            }
//        });
//
//    }


    }


