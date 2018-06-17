package com.company.superandrieiev.insystemtask.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DIMON on 15.06.2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "my.db";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Contract.Entry.SQL_CREATE_FAVORITE_TABLE);
    }

    private String DROP_TABLE = "DROP TABLE IF EXISTS " + Contract.Entry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db1, int oldVersion, int newVersion) {
        db1.execSQL(DROP_TABLE);
        onCreate(db1);
    }

    public void addRecord(ImageWithTag imageWithTag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.Entry.COLUMN_URI, String.valueOf(imageWithTag.getImageURI()));
        values.put(Contract.Entry.COLUMN_TAGS, imageWithTag.getTags());

        db.insert(Contract.Entry.TABLE_NAME, null, values);
        db.close();
    }


    public List<ImageWithTag> getAllRecords() {
        String[] columns = {
                Contract.Entry.COLUMN_URI,
                Contract.Entry.COLUMN_TAGS,
        };

        List<ImageWithTag> imageWithTagList = new ArrayList<ImageWithTag>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Contract.Entry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                ImageWithTag imageWithTag = new ImageWithTag();
                imageWithTag.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_URI))));
                imageWithTag.setTags(cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_TAGS)));
                imageWithTagList.add(imageWithTag);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return imageWithTagList;
    }

}

