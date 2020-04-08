package com.example.courseregistrationwaitinglist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class StudentCursorAdapter extends CursorAdapter {
    public StudentCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c , flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String studentFn = cursor.getString(cursor.getColumnIndex(DBopenHelper.STUDENT_FN));
        String studentLn = cursor.getString(cursor.getColumnIndex(DBopenHelper.STUDENT_LN));
        String studentPr = cursor.getString(cursor.getColumnIndex(DBopenHelper.STUDENT_PRIORITY));
        int pos = studentFn.indexOf(10);
        if (pos != -1) {
            studentFn = studentFn.substring(0, pos) + "...";
        }
        pos = studentLn.indexOf(10);
        if (pos != -1) {
            studentLn = studentLn.substring(0, pos) + "...";
        }
        pos = studentPr.indexOf(10);
        if (pos != -1) {
            studentPr = studentPr.substring(0, pos) + "...";
        }

        TextView tv = view.findViewById(R.id.tv_fn);
        tv.setText(studentFn);
        TextView ln = view.findViewById(R.id.tv_ln);
        ln.setText(studentLn);
        TextView pr = view.findViewById(R.id.tv_pr);
        pr.setText(studentPr);

    }
}
