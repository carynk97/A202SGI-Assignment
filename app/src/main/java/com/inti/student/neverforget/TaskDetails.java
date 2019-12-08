package com.inti.student.neverforget;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class TaskDetails extends AppCompatActivity {

    ListDatabase mListDatabase;
    private String mTask;
    private String mDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TextView mAddedTask = (TextView)findViewById(R.id.tvAddedDetails);
        TextView mAddedDate = (TextView)findViewById(R.id.tvAddedDate);
        Button mBtnDelete = (Button)findViewById(R.id.btnDelete);
        Button mBtnBack = (Button)findViewById(R.id.btnBack);

        mListDatabase = new ListDatabase(this);

        Intent intent = getIntent();
        mTask = intent.getStringExtra("TaskName");
        Cursor data = mListDatabase.readTable();
        while (data.moveToNext()) {
            if (data.getString(1).equals(mTask)) {
                mDate = data.getString(2);
            }
            else {
                continue;
            }
        }

        mAddedTask.setText(mTask);
        mAddedDate.setText(mDate);

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mListDatabase.deleteTask(mTask);
                 Toast.makeText(TaskDetails.this,"Task done and dusted!", Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(TaskDetails.this, MainActivity.class));
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TaskDetails.this, MainActivity.class));
            }
        });
    }


}
