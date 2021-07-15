package com.example.findadog;

import java.io.Serializable;
import java.util.ArrayList;

public class User  implements Serializable {
    String userName;
    String userId;
    String uriImage;

    public User(String userName, String userId,   String uriImage) {
        this.userName = userName;
        this.userId = userId;
        this.uriImage=uriImage;
    }

    public User() {

    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
