package com.example.courseregistrationwaitinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int EDITOR_CODE_REQUEST = 1001;
    private CursorAdapter cursorAdapter;
    private Toolbar mTopToolbar;
    private String sortBy = DBopenHelper.STUDENT_FN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** after querying table will get cursor object ,so declare cursor
         *
         */
        Cursor cursor;

        /**
         * id of textview in layout file
         */

        cursorAdapter = new StudentCursorAdapter(this,null,0);

        /**
         * reference to listview
         */
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);
        /**
         * when user selects one row from listview below clicklistener called
         */
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new  Intent(MainActivity.this, editor.class);
                Uri uri = Uri.parse(StudentCourseProvider.CONTENT_URI+ "/" +id);
                intent.putExtra(StudentCourseProvider.CONTENT_ITEM_TYPE,uri);
                startActivityForResult(intent, EDITOR_CODE_REQUEST);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);
        mTopToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);

    }

    private void insertstudent(String studentfn, String studentln, String studentPriority) {
        ContentValues values = new ContentValues();

        DBopenHelper dBopenHelper = new DBopenHelper(getApplicationContext());


        values.put(DBopenHelper.STUDENT_FN, studentfn);
        values.put(DBopenHelper.STUDENT_LN, studentln);
        values.put(DBopenHelper.STUDENT_PRIORITY, studentPriority);
        /**
         * content provider already registered in manifest file
         */
        Uri studentUri = getContentResolver().insert(StudentCourseProvider.CONTENT_URI, values);
        /**
         * log debug the mainactivity log.d
         */

        Log.d("MainActivity", "inserted student" + studentUri.getLastPathSegment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_create_sample:

                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteAllStudents();
                break;
            case R.id.action_sort_by_fn:
                sortBy = DBopenHelper.STUDENT_FN;
                restartLoader();
                break;
            case R.id.action_sort_by_ln:
                sortBy = DBopenHelper.STUDENT_LN;
                restartLoader();
                break;
            case R.id.action_sort_by_pr:
                sortBy = DBopenHelper.STUDENT_PRIORITY;
                restartLoader();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    private void deleteAllStudents() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            // Data management code is here
                            getContentResolver().delete(StudentCourseProvider.CONTENT_URI,
                                    null, null);
                            restartLoader();
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setTitle(getString(R.string.delete_all_title))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }
    private void insertSampleData() {
        insertstudent("Peter", "Thomas", "1st grade");
        insertstudent("Sophia", "Lee", "2nd grade");
        insertstudent("Emma", "Victor", "1st grade");
        insertstudent("Isabella", "Trump", "3rd grade");
        insertstudent("Tom", "Brandy", "4th grade");
        insertstudent("Aron", "Newton", "3rd grade");
        insertstudent("Aron", "Bress", "2nd grade");
        insertstudent("Bill", "Romo", "4th grade");
        insertstudent("Russell", "Beckham Jr.", "4th grade");
        insertstudent("Ben", "Rick", "Graduate");
        // after change in data , inform dataloader to restart loader to reload data

        restartLoader();
    }
    private Loader<Cursor> restartLoader() {
        return getSupportLoaderManager().restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, StudentCourseProvider.CONTENT_URI,
                null, null, null, sortBy.concat(" ASC"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);

    }
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void openEditorNewStudent(View view) {
        Intent intent = new Intent(this,editor.class);
        startActivityForResult(intent, EDITOR_CODE_REQUEST);
    }
    @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

       if (requestCode == EDITOR_CODE_REQUEST && resultCode == RESULT_OK) {
           restartLoader();
        }
    }
}
