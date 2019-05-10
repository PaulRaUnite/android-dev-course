package com.example.contactslistdemo.presenter;

import android.net.Uri;

public class Contact {

    public Uri profilePic;
    public String name;

    @Override
    public String toString() {
        return "Contact{" +
                "profilePic=" + profilePic +
                ", name='" + name + '\'' +
                '}';
    }
}
