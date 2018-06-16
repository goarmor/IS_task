package com.company.superandrieiev.insystemtask.MVP;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.company.superandrieiev.insystemtask.sql.DatabaseHelper;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by DIMON on 16.06.2018.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {

    private DatabaseHelper databaseHelper;
    private Context context;

    public MainPresenter(Context context) {
        this.context = context;
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

    public void startImagePick () {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");

        getView().showImageSelection(photoPickerIntent, 1);
    }

    public void listFiltering(String query) {
        getView().showFiltratedList(query);
    }

    public void convertUriToBitmap(Intent imageReturnedIntent) throws FileNotFoundException {
        Uri imageUri = imageReturnedIntent.getData();
        final InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        getView().showAlertDialog(selectedImage, imageUri);
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
