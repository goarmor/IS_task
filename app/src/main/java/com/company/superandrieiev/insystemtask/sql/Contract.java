package com.company.superandrieiev.insystemtask.sql;

import android.provider.BaseColumns;

/**
 * Created by DIMON on 15.06.2018.
 */

public class Contract {
    public static final class Entry implements BaseColumns {
        public static final String TABLE_NAME = "images_with_tags";
        public static final String COLUMN_URI = "uri";
        public static final String COLUMN_TAGS = "tags";
        public static final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + Contract.Entry.TABLE_NAME + " (" +
                Contract.Entry.COLUMN_URI + " TEXT NOT NULL," +
                Contract.Entry.COLUMN_TAGS + " TEXT NOT NULL " +
                "); ";
    }
}
