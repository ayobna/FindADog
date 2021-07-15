package com.example.findadog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
//
public class DogInfoAdapter extends ArrayAdapter<DogInfo> {
    ArrayList<DogInfo> dogInfo;
    Context context;
    public DogInfoAdapter(@NonNull Context context, ArrayList<DogInfo> dogInfo) {
        super(context, R.layout.info_dog_card,dogInfo);
        this.dogInfo=dogInfo;
        this.context=context;
    }
    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.info_dog_card,null,true);
        TextView txtDogName=rowView.findViewById(R.id.txtDogName);
        ImageView imgDogInfo=rowView.findViewById(R.id.imgDogInfo);
        if (dogInfo.get(position).getUriImage()!=null) {
            Picasso.get().load(dogInfo.get(position).getUriImage().get(0)).into(imgDogInfo);

        }
        txtDogName.setText(dogInfo.get(position).getDogName());

        return  rowView;
    }
}
