package com.company.superandrieiev.insystemtask.MVP;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.company.superandrieiev.insystemtask.R;
import com.company.superandrieiev.insystemtask.adapter.RecyclerAdapter;
import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private final int Pick_image = 1;
    private ArrayList<ImageWithTag> imageWithTagsList;
    private RecyclerAdapter recyclerAdapter;
    private AlertDialog alertDialog;

    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = (ImageView) findViewById(R.id.imageView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        imageWithTagsList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(imageWithTagsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        //Связываемся с нашей кнопкой Button:
        Button pickImage = (Button) findViewById(R.id.add_picture_but);
        //Настраиваем для нее обработчик нажатий OnClickListener:
        pickImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });

        sv = (SearchView) findViewById(R.id.search_view);

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                recyclerAdapter.getFilter().filter(query);
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
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        //создаем алерт с выбранным изображением
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

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }



    @Override
    public void showImageSelection() {

    }

    @Override
    public void closeAlertDialog() {
        if(alertDialog != null)
        alertDialog.dismiss();
    }

    @Override
    public void showActualImagesWithTags(ArrayList<ImageWithTag> imagesWithTags) {
        imagesWithTags.clear();
        imageWithTagsList.addAll(imagesWithTags);
        recyclerAdapter.notifyDataSetChanged();
    }
}
