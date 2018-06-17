package com.company.superandrieiev.insystemtask.test;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.company.superandrieiev.insystemtask.sql.Contract;

import org.mockito.Mockito;

/**
 * Created by DIMON on 17.06.2018.
 */

public class TestDatabasehelper {

    SQLiteDatabase db;
    ContentValues values;


    public TestDatabasehelper(SQLiteDatabase mockSQ) {
       db = mockSQ;
    }


    public void addRecord(ImageWithTag imageWithTag) {
        values = Mockito.mock(ContentValues.class);
        db.insert(Contract.Entry.TABLE_NAME, null, values);
        db.close();
    }


    public ContentValues getValuesParameter() {
        return values;
    }

}
