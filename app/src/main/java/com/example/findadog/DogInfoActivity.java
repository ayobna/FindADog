package com.example.findadog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DogInfoActivity extends AppCompatActivity {
TextView txtDogName,txtGeneralDescription, txtCharacteristics,txtHealth,txtTraining;
    DogInfo dogInfo;
    ImageSlider imageSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);
        initViews();
        Intent i = getIntent();
        dogInfo  = (DogInfo) i.getSerializableExtra("DogInfo");

// the slide images an the top of tha page
        List<SlideModel> slideModels = new ArrayList<>();
        for (int k = 0; k < dogInfo.getUriImage().size(); k++) {
            slideModels.add(new SlideModel(dogInfo.getUriImage().get(k), "",ScaleTypes.FIT));
        }
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);



    txtGeneralDescription.setText(dogInfo.getGeneralDescription());
    txtCharacteristics.setText(dogInfo.getCharacteristics());
     txtHealth.setText(dogInfo.getHealth());
     txtTraining.setText(dogInfo.getTraining());
     txtDogName.setText(dogInfo.getDogName());
    }

    private void initViews() {
        txtDogName = findViewById(R.id.txtDogName);
        txtGeneralDescription = findViewById(R.id.txtGeneralDescription);
        txtCharacteristics = findViewById(R.id.txtCharacteristics);
        txtHealth = findViewById(R.id.txtHealth);
        txtTraining = findViewById(R.id.txtTraining);
        imageSlider = findViewById(R.id.slider);
    }
}