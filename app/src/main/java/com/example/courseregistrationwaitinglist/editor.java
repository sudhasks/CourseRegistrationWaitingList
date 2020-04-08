package com.example.courseregistrationwaitinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class editor extends AppCompatActivity {

    private String action;
    private EditText editor_FN;
    private EditText editor_LN;
    private EditText editor_PR;
    private RadioGroup editor_RG;
    private String studentFilter;
    private String oldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_done_white_24dp);
        editor_FN = findViewById(R.id.editFN);
        editor_LN = findViewById(R.id.editLN);
        editor_PR= findViewById(R.id.editPR);
        editor_RG = findViewById(R.id.rg_edit);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(StudentCourseProvider.CONTENT_ITEM_TYPE);


        editor_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                editor_PR.setText(((RadioButton)findViewById(editor_RG.getCheckedRadioButtonId())).getText().toString());
            }
        });

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_student));
        }

        else {
            action = Intent.ACTION_EDIT;
            studentFilter = DBopenHelper.STUDENT_ID+ "=" +uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri,
                    DBopenHelper.ALL_COLUMNS, studentFilter, null, null);
            cursor.moveToFirst();
            /**
             * retrive text of selected student
             */
            oldText = cursor.getString(cursor.getColumnIndex(DBopenHelper.STUDENT_FN));
            /**
             * set selected text to editor_FN activity as it is
             */
            String pr = cursor.getString(cursor.getColumnIndex(DBopenHelper.STUDENT_PRIORITY));

            editor_FN.setText(oldText);
            editor_FN.requestFocus();
            editor_LN.setText(cursor.getString(cursor.getColumnIndex(DBopenHelper.STUDENT_LN)));
            editor_PR.setText(pr);

            if(pr.startsWith("1")){
                ((RadioButton)findViewById(R.id.radioButton1)).setChecked(true);
            } else if(pr.startsWith("2")){
                ((RadioButton)findViewById(R.id.radioButton2)).setChecked(true);
            } else if(pr.startsWith("3")){
                ((RadioButton)findViewById(R.id.radioButton3)).setChecked(true);
            } else if(pr.startsWith("4")){
                ((RadioButton)findViewById(R.id.radioButton4)).setChecked(true);
            } else {
                ((RadioButton)findViewById(R.id.radioButton5)).setChecked(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /**
         * trashcan icon display on existing note only
         */
        if (action.equals(Intent.ACTION_EDIT)) {

            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finishEditing();
        }
        else {
            deleteStudent();
        }

        return true;
    }
    private void deleteStudent() {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            getContentResolver().delete(StudentCourseProvider.CONTENT_URI,studentFilter, null);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setTitle(getString(R.string.delete_student_title))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }

    private void finishEditing() {
        String newText = editor_FN.getText().toString().trim();
        String studentln = editor_LN.getText().toString().trim();
        String studentPriority = ((RadioButton)findViewById(editor_RG.getCheckedRadioButtonId())).getText().toString();

        boolean isDeleting = false;
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0
                        || studentln.length() == 0
                        || studentPriority.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    InsertStudent(newText, studentln, studentPriority);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0
                        || studentln.length() == 0
                        || studentPriority.length() == 0)
                {
                    isDeleting = true;
                    deleteStudent();
                } else
                {
                    updateStudent(newText, studentln, studentPriority);
                }



        }
        if (!isDeleting)
            finish();
    }
    private void updateStudent(String studentfn, String studentln, String studentPriority) {
        ContentValues values = new ContentValues();

        DBopenHelper dBopenHelper = new DBopenHelper(getApplicationContext());

        values.put(DBopenHelper.STUDENT_FN, studentfn);
        values.put(DBopenHelper.STUDENT_LN, studentln);
        values.put(DBopenHelper.STUDENT_PRIORITY, studentPriority);
        /**
         * student filter checks one selected row
         */
        getContentResolver().update(StudentCourseProvider.CONTENT_URI, values,studentFilter,null);
        Toast.makeText(this,getString(R.string.student_updated),Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);



    }
    private void InsertStudent(String studentfn, String studentln, String studentPriority) {

        ContentValues values = new ContentValues();

        DBopenHelper dBopenHelper = new DBopenHelper(getApplicationContext());

        values.put(DBopenHelper.STUDENT_FN, studentfn);
        values.put(DBopenHelper.STUDENT_LN, studentln);
        values.put(DBopenHelper.STUDENT_PRIORITY, studentPriority);
        /**
         * content provider already registered in manifest file
         */
        getContentResolver().insert(StudentCourseProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }
    @Override
    public void onBackPressed () {
        finishEditing();
    }

}
