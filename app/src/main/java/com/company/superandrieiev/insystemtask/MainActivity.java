package com.company.superandrieiev.insystemtask;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.company.superandrieiev.insystemtask.adapter.RecyclerAdapter;
import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.company.superandrieiev.insystemtask.sql.DatabaseHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private final int Pick_image = 1;
    private DatabaseHelper databaseHelper;
    private ArrayList<ImageWithTag> imageWithTagsList;
    private RecyclerAdapter recyclerAdapter;

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
        Button PickImage = (Button) findViewById(R.id.add_picture_but);
        //Настраиваем для нее обработчик нажатий OnClickListener:
        PickImage.setOnClickListener(new OnClickListener() {

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

        databaseHelper = new DatabaseHelper(this);
        getDataFromSQLite();
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
                        final Button alertButton = (Button)view.findViewById(R.id.add_picture_but);
                        addImageDialog.setView(view);
                        final AlertDialog ad = addImageDialog.show();
                        alertButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String userTags = edit_text_view.getText().toString();
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

                                    ad.dismiss();
                                }
                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    private void getDataFromSQLite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                imageWithTagsList.clear();
                imageWithTagsList.addAll(databaseHelper.getAllRecords());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recyclerAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void postDataToSQLite(Uri imageURI, String tags) {
        ImageWithTag imageWithTag = new ImageWithTag();
        imageWithTag.setImageURI(imageURI);
        imageWithTag.setTags(tags);
        databaseHelper.addRecord(imageWithTag);
    }
}
