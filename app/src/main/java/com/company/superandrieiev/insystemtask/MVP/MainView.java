package com.company.superandrieiev.insystemtask.MVP;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by DIMON on 16.06.2018.
 */

public interface MainView extends MvpView {

    void showImageSelection();

    void closeAlertDialog();

    void showActualImagesWithTags(ArrayList<ImageWithTag> imagesWithTags);



}
