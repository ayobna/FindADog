package com.example.findadog;

import java.io.Serializable;
import java.util.ArrayList;
// a class for the information on the dogs breed
public class DogInfo implements Serializable {
    String dogName;
    String GeneralDescription;
    String Characteristics;
    String Health;
    String Training;

    ArrayList<String> UriImage;

    public  DogInfo(){}

    public DogInfo(String dogName, String generalDescription, String characteristics, String health, String training, ArrayList<String> UriImage) {
        this.dogName = dogName;
        GeneralDescription = generalDescription;
        Characteristics = characteristics;
        Health = health;
        Training = training;
        this.UriImage = UriImage;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getGeneralDescription() {
        return GeneralDescription;
    }

    public void setGeneralDescription(String generalDescription) {
        GeneralDescription = generalDescription;
    }

    public String getCharacteristics() {
        return Characteristics;
    }

    public void setCharacteristics(String characteristics) {
        Characteristics = characteristics;
    }

    public String getHealth() {
        return Health;
    }

    public void setHealth(String health) {
        Health = health;
    }

    public String getTraining() {
        return Training;
    }

    public void setTraining(String training) {
        Training = training;
    }

    public ArrayList<String> getUriImage() {
        return UriImage;
    }

    public void setUriImage(ArrayList<String> uriImage) {
        UriImage = uriImage;
    }
}
