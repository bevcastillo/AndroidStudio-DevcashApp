package com.example.devcash.Database;


import com.firebase.client.Firebase;

public class FirebaseDB extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }
}
