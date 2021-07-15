package com.example.findadog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static com.facebook.FacebookSdk.getApplicationContext;


public class UserPostAdapter extends ArrayAdapter<PostADog> {
    ArrayList<PostADog> postADogs;
    Context context;
    ListView listView;
    UserPostAdapter userPostAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    PostADog postToRemove;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    ArrayList<PostADog> postADogList;
    ImageView btnRemove;
    public UserPostAdapter(@NonNull Context context, ArrayList<PostADog> postADogs) {
        super(context,R.layout.each_row_in_user_post_dog,postADogs);
        this.postADogs=postADogs;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.each_row_in_user_post_dog,null,true);
        TextView bigText=rowView.findViewById(R.id.bigText);
        TextView smallText=rowView.findViewById(R.id.smallText);
        ImageView imageView1=rowView.findViewById(R.id.postDogListImage);
        ImageButton imageView2=rowView.findViewById(R.id.btnRemove);
        if ( postADogs.get(position).getUriImage()!=null){
            Picasso.get().load(postADogs.get(position).getUriImage().get(0)).into(imageView1);
        }
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postToRemove=postADogs.get(position);
                Toast.makeText(getApplicationContext(),postToRemove.getGender(),Toast.LENGTH_LONG).show();
                  removeFromDb();
            }
        });
        bigText.setText( postADogs.get(position).getBreedType() );
        smallText.setText( postADogs.get(position).getGender());
        return  rowView;
    }


    private void removeFromDb() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference("PostDog");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dp : snapshot.getChildren()) {
                    PostADog temp = dp.getValue(PostADog.class);
                    if (postToRemove.getUserPostId().equals(temp.getUserPostId())) {
                        dp.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
