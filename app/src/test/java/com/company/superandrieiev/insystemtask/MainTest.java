package com.company.superandrieiev.insystemtask;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.company.superandrieiev.insystemtask.model.ImageWithTag;
import com.company.superandrieiev.insystemtask.mvp.MainView;
import com.company.superandrieiev.insystemtask.sql.Contract;
import com.company.superandrieiev.insystemtask.sql.DatabaseHelper;
import com.company.superandrieiev.insystemtask.test.TestDatabasehelper;
import com.company.superandrieiev.insystemtask.test.TestPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import static junit.framework.Assert.assertNotNull;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(JUnit4.class)
public class MainTest {

    private MainView mainView;
    private TestPresenter testPresenter;
    private DatabaseHelper databaseHelper;
    private TestDatabasehelper testDatabasehelper;
    private SQLiteDatabase sqLiteDatabase;


    @Before
    public void setUp() throws Exception {
        mainView = Mockito.mock(MainView.class);
        databaseHelper = Mockito.mock(DatabaseHelper.class);
        testPresenter = new TestPresenter(mainView, databaseHelper);
        sqLiteDatabase = Mockito.mock(SQLiteDatabase.class);
        testDatabasehelper = new TestDatabasehelper(sqLiteDatabase);
    }


    @Test
    public void testCreated() throws Exception {
        assertNotNull(testPresenter);
    }


    @Test
    public void postDataToSQLite() throws Exception {
        Uri uri = Mockito.mock(Uri.class);
        testPresenter.postDataToSQLite(uri, "tags");

        Mockito.verify(databaseHelper).addRecord(testPresenter.getImageWithTagParameter());
    }


    @Test
    public void testImagePick() throws Exception {
        testPresenter.startImagePick();

        Mockito.verify(mainView).showImageSelection(testPresenter.getTestPhotoPickerIntent(), 1);
    }


    @Test
    public void testAddRecord() throws Exception {
       testDatabasehelper.addRecord(new ImageWithTag());

        Mockito.verify(sqLiteDatabase).insert(Contract.Entry.TABLE_NAME, null, testDatabasehelper.getValuesParameter());
        Mockito.verify(sqLiteDatabase).close();
    }

}