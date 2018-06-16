package com.company.superandrieiev.insystemtask.MVP;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.company.superandrieiev.insystemtask.sql.DatabaseHelper;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;

/**
 * Created by DIMON on 16.06.2018.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private DatabaseHelper databaseHelper;

    public MainPresenter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void addImageWithTag(String alertTags, Uri imageUri) {
        String userTags = alertTags;
        String tags = "";
        String[] arr = userTags.split(",");
        for (int i = 0; i < arr.length; i++) {
            String tag = arr[i].trim();
            if (!tag.contains(" ") && !tag.isEmpty()) {
                tags += arr[i] + ", ";
            }
        }
        tags = tags.substring(0, tags.length() - 2);
        if (tags.length() > 0) {
            postDataToSQLite(imageUri, tags);
            getDataFromSQLite();

            getView().closeAlertDialog();
        }
    }

    public void setActualImagesWithTag() {
        getDataFromSQLite();
    }


    private void postDataToSQLite(Uri imageURI, String tags) {
        ImageWithTag imageWithTag = new ImageWithTag();
        imageWithTag.setImageURI(imageURI);
        imageWithTag.setTags(tags);
        databaseHelper.addRecord(imageWithTag);
    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, ArrayList<ImageWithTag>>() {
            @Override
            protected ArrayList<ImageWithTag> doInBackground(Void... params) {
                 return (ArrayList<ImageWithTag>)databaseHelper.getAllRecords();
            }

            @Override
            protected void onPostExecute(ArrayList<ImageWithTag> imagesWithTags) {
                super.onPostExecute(imagesWithTags);
                getView().showActualImagesWithTags(imagesWithTags);
            }
        }.execute();
    }

}
