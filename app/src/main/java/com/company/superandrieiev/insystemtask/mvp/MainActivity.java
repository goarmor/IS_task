package com.company.superandrieiev.insystemtask.mvp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.superandrieiev.insystemtask.R;
import com.company.superandrieiev.insystemtask.adapter.RecyclerAdapter;
import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.io.FileNotFoundException;
import java.util.ArrayList;


public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {

    private RecyclerView recyclerView;
    private final int Pick_image = 1;
    private SearchView sv;
    private Button pickImage;
    private TextView emptyText;
    private ArrayList<ImageWithTag> imageWithTagsList;
    private RecyclerAdapter recyclerAdapter;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyText = (TextView) findViewById(R.id.text_empty);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imageWithTagsList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(imageWithTagsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);

        pickImage = (Button) findViewById(R.id.add_picture_but);
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().startImagePick();
            }
        });

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        sv = (SearchView) findViewById(R.id.search_view);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Фильтруем при вводе текста
                getPresenter().listFiltering(query);
                return false;
            }
        });

        getPresenter().setActualImagesWithTag();
    }


    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }


    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        getPresenter().convertUriToBitmap(imageReturnedIntent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }


    @Override
    public void showImageSelection(Intent photoPickerIntent, int pick_image) {
        startActivityForResult(photoPickerIntent, pick_image);
    }


    @Override
    public void closeAlertDialog() {
        alertDialog.dismiss();
    }


    @Override
    public void showActualImagesWithTags(ArrayList<ImageWithTag> imagesWithTags) {
        if (imagesWithTags.size() == 0) {
            emptyText.setVisibility(View.VISIBLE);
            sv.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.INVISIBLE);
            sv.setVisibility(View.VISIBLE);
        }

        imageWithTagsList.clear();
        imageWithTagsList.addAll(imagesWithTags);
        recyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void showFiltratedList(String query) {
        recyclerAdapter.getFilter().filter(query);
    }


    @Override
    public void showAlertDialog(Bitmap selectedImage, final Uri imageUri) {
        final AlertDialog.Builder addImageDialog = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_image_alert, null);
        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        image.setImageBitmap(selectedImage);

        final EditText edit_text_view = (EditText)view.findViewById(R.id.edit_text_view);
        final Button alertButton = (Button)view.findViewById(R.id.add_picture_alert_but);

        addImageDialog.setView(view);
        alertDialog = addImageDialog.show();

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().addImageWithTag(edit_text_view.getText().toString(), imageUri);
            }
        });
    }
}
