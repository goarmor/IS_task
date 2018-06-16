package com.company.superandrieiev.insystemtask.MVP;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by DIMON on 16.06.2018.
 */

public interface MainView extends MvpView {

    void showImageSelection(Intent photoPickerIntent, int pick_image);

    void closeAlertDialog();

    void showActualImagesWithTags(ArrayList<ImageWithTag> imagesWithTags);

    void showFiltratedList(String query);

    void showAlertDialog(Bitmap selectedImage, final Uri imageUri);

}
