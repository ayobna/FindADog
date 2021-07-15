package com.example.findadog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class PostDogListAdapter extends ArrayAdapter<PostADog> {
ArrayList<PostADog> postADogs;
Context context;
    public PostDogListAdapter(@NonNull Context context, ArrayList<PostADog> postADogs) {
        super(context,R.layout.each_row_in_post_dog,postADogs);
        this.postADogs=postADogs;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position,  View view,  ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.each_row_in_post_dog,null,true);
        TextView bigText=rowView.findViewById(R.id.bigText);
        TextView smallText=rowView.findViewById(R.id.smallText);
        ImageView imageView1=rowView.findViewById(R.id.postDogListImage);
        if ( postADogs.get(position).getUriImage()!=null){
            Picasso.get().load(postADogs.get(position).getUriImage().get(0)).into(imageView1);
        }
        bigText.setText( postADogs.get(position).getBreedType() );
        smallText.setText( postADogs.get(position).getGender());
        return  rowView;
    }

}
