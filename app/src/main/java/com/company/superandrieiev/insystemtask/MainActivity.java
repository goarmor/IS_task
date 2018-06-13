package com.company.superandrieiev.insystemtask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private final int Pick_image = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Связываемся с нашим ImageView:
        imageView = (ImageView)findViewById(R.id.imageView);

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
    }

    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        //создаем алерт с выбранным изображением
                        AlertDialog.Builder addImageDialog = new AlertDialog.Builder(this);
                        View view = getLayoutInflater().inflate(R.layout.add_image_alert, null);
                        ImageView image = view.findViewById(R.id.imageView);
                        image.setImageBitmap(selectedImage);
                        EditText edit_text_view = view.findViewById(R.id.edit_text_view);
                        final Button alertButton = view.findViewById(R.id.add_picture_but);
                        edit_text_view.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (s)
                                alertButton.setEnabled(true);
                                else 
                                alertButton.setEnabled(false);
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {}

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        });

                        alertButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        addImageDialog.setView(view);
                        addImageDialog.show();


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }}
}