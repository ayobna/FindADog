package com.example.findadog;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class PostADog  implements Serializable {
    String userPostId;
    String breedType;
   String birthday ;
   String gender;
   String address;
   String description;
   Double price;
   String phone;
   ArrayList<String> uriImage;
   int idPost=0;
    public PostADog() {
    }

    public PostADog(String userPostId, String breedType, String birthday, String gender, String address, String description, Double price,String phone,  ArrayList<String>  uriImage) {
        this.userPostId = userPostId;
        this.breedType = breedType;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.description = description;
        this.price = price;
        this.phone=phone;
        this.uriImage=uriImage;
        idPost++;
    }



    public String getUserPostId() {
        return userPostId;
    }

    public void setUserPostId(String userPostId) {
        this.userPostId = userPostId;
    }

    public String getBreedType() {
        return breedType;
    }

    public void setBreedType(String breedType) {
        this.breedType = breedType;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public ArrayList<String> getUriImage() {
        return uriImage;
    }

    public void setUriImage(ArrayList<String> uriImage) {
        this.uriImage = uriImage;
    }

    public  int getIdPost() {
        return idPost;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
