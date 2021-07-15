package com.example.findadog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.findadog.PostDogActivity.GET_IMAGE_FROM_GALLERY;

public class EditProfileActivity extends AppCompatActivity  implements View.OnClickListener{

    CircleImageView imageProfile;
    Button btnPicImage,btnTakeImage,btnSave,btnCancel;
    EditText txtUserName;
    private static final int CAMERA_REQUEST_CODE=101;
    final  static  int GET_IMAGE_FROM_GALLERY=1;
    Bitmap imageBitmap;
    User user;
    Uri imageUri =null;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
       // myRef=database.getReference("User");
        myRef = database.getReference("User").child(mAuth.getUid());
        Intent i = getIntent();
        user  = (User) i.getSerializableExtra("User");
        initViews();
        initButtons();
        userInfo();
    }

    private void userInfo() {
        txtUserName.setText(user.getUserName());
        if (!imageProfile.equals(null))
            Picasso.get().load(user.getUriImage()).into(imageProfile);

    }


    private void initViews() {
        txtUserName = findViewById(R.id.txtUserName);
        imageProfile=findViewById(R.id.imageProfile);
        btnPicImage=findViewById(R.id.btnPicImage);
        btnTakeImage=findViewById(R.id.btnTakeImage);
        btnSave=findViewById(R.id.btnSave);
        btnCancel=findViewById(R.id.btnCancel);
    }
    private  void  initButtons(){
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnTakeImage.setOnClickListener(this);
        btnPicImage.setOnClickListener(this);
    }
    private void OpenCamera() {
        Intent open_Camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(open_Camera,CAMERA_REQUEST_CODE);
    }
    private void getImageFromGallery() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        intent.putExtra(intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,GET_IMAGE_FROM_GALLERY);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode) {
            case CAMERA_REQUEST_CODE :
                if (resultCode == RESULT_OK) {
                    imageProfile.setDrawingCacheEnabled(true);
                    imageProfile.buildDrawingCache();
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    imageProfile.setImageBitmap(imageBitmap);
                    imageUri =getImageUri(EditProfileActivity.this,imageBitmap);
                }
            break;
            case GET_IMAGE_FROM_GALLERY :
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Picasso.get().load(imageUri).into(imageProfile);
                }
                break;
        }
       // UploadImage();
    }

    private void UploadImage() {

            progressDialog =new ProgressDialog(EditProfileActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);

            storageReference= FirebaseStorage.getInstance().getReference().child("Users_image");
            Uri individualImage=imageUri;
            final StorageReference imageName=storageReference.child( mAuth.getCurrentUser().getEmail());
            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                         user.setUriImage(uri.toString());
                            progressDialog.dismiss();
                            myRef.setValue(user);
                            startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
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

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btnCancel:
                startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
                break;
            case R.id.btnSave:
                Save();
                break;
            case R.id.btnTakeImage:
                OpenCamera();
                break;
            case R.id.btnPicImage:
                getImageFromGallery();
                break;

        }
    }

    private void Save() {
        user.setUserName(txtUserName.getText().toString());
        if (imageUri != null)
            UploadImage();
        else {
            myRef.setValue(user);
            startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
        }
    }
}