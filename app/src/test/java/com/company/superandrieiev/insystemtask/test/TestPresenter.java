package com.company.superandrieiev.insystemtask.test;

import android.content.Intent;
import android.net.Uri;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.company.superandrieiev.insystemtask.mvp.MainView;
import com.company.superandrieiev.insystemtask.sql.DatabaseHelper;

import org.mockito.Mockito;

/**
 * Created by DIMON on 17.06.2018.
 */

public class TestPresenter {

    private MainView mockMainView;
    private DatabaseHelper mockDatabaseHelper;
    private Intent testPhotoPickerIntent;
    private ImageWithTag imageWithTag;


    public TestPresenter(MainView mockMainView, DatabaseHelper mockDatabaseHelper) {
        this.mockDatabaseHelper = mockDatabaseHelper;
        this.mockMainView = mockMainView;
    }


    public void startImagePick () {
        testPhotoPickerIntent = Mockito.mock(Intent.class);
        testPhotoPickerIntent.setType("image/*");
        mockMainView.showImageSelection(testPhotoPickerIntent, 1);
    }


    public void postDataToSQLite(Uri imageURI, String tags) {
        imageWithTag = Mockito.mock(ImageWithTag.class);
        imageWithTag.setImageURI(imageURI);
        imageWithTag.setTags(tags);
        mockDatabaseHelper.addRecord(imageWithTag);
    }


    public Intent getTestPhotoPickerIntent() {
        return testPhotoPickerIntent;
    }


    public ImageWithTag getImageWithTagParameter() {
        return imageWithTag;
    }

}
