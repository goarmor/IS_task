package com.company.superandrieiev.insystemtask.model;


import android.net.Uri;

/**
 * Created by DIMON on 15.06.2018.
 */

public class ImageWithTag {

    private Uri imageURI;
    private String tags;

    public String getTags() {
        return tags;
    }

    public Uri getImageURI() {
        return imageURI;
    }

    public void setImageURI(Uri imageURI) {
        this.imageURI = imageURI;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
