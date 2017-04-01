package com.example.android.makeathon;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by hp pc on 4/1/2017.
 */

@IgnoreExtraProperties
public class User {

    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        this.email = email;
    }
}
