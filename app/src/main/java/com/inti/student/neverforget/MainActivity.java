package com.inti.student.neverforget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ListView mFirstTask;
    ListDatabase mListDatabase;
    ImageButton mImageButton;
    Calendar c;
    DatePickerDialog mDatePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListDatabase = new ListDatabase(this);

        mFirstTask = (ListView)findViewById(R.id.firstTask);

        loadTaskList();

        mFirstTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String textName;
                textName = parent.getItemAtPosition(position).toString();
                String[] words = textName.split("=");
                words[2] = words[2].substring(0, words[2].length() - 1);
                Intent intent = new Intent(MainActivity.this, TaskDetails.class);
                intent.putExtra("TaskName", words[2]);
                startActivity(intent);
            }
        });

        mImageButton = (ImageButton)findViewById(R.id.floatingActionBtn);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.add_task_dialog, null);
                final EditText mAddTask = (EditText) mView.findViewById(R.id.etTask);
                final TextView mDatePicker = (TextView) mView.findViewById(R.id.tvDatePicker);
                Button mAddButton = (Button) mView.findViewById(R.id.btnAdd);

                mDatePicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        c = Calendar.getInstance();
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int month = c.get(Calendar.MONTH);
                        int year = c.get(Calendar.YEAR);

                        mDatePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                                mDatePicker.setText(mDayOfMonth + "/" + (mMonth + 1) + "/" + mYear);
                            }
                        }, day, month, year);

                        mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        mDatePickerDialog.show();
                    }
                });

                mAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mAddTask.getText().toString().isEmpty() && mDatePicker.getText().toString().isEmpty()){
                            String task = String.valueOf(mAddTask.getText());
                            String date = String.valueOf(mDatePicker.getText());
                            mListDatabase.insertNewTask(task, date);
                            Toast.makeText(MainActivity.this, R.string.task_added_WITHOUT_msg, Toast.LENGTH_SHORT).show();
                            loadTaskList();
                        }
                        else if (!mAddTask.getText().toString().isEmpty() && !mDatePicker.getText().toString().isEmpty()){
                            String task = String.valueOf(mAddTask.getText());
                            String date = String.valueOf(mDatePicker.getText());
                            mListDatabase.insertNewTask(task, date);
                            Toast.makeText(MainActivity.this, R.string.task_added_msg, Toast.LENGTH_SHORT).show();
                            loadTaskList();
                        }
                        else{
                            Toast.makeText(MainActivity.this, R.string.task_empty_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

    }

    private void loadTaskList()
    {
        Cursor data = mListDatabase.readTable();
        HashMap<String, String> list = new HashMap<>();
        while(data.moveToNext()){
            list.put(data.getString(1), data.getString(2));
        }

        List<HashMap<String, String>> ListItems = new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this, ListItems, R.layout.added_task_list,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.tvTaskDetails, R.id.tvDeadline});

        Iterator it = list.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String, String> resultMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            ListItems.add(resultMap);
        }

        mFirstTask.setAdapter(adapter);
    }


    public void btnLightMode_clicked(View view){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void btnDarkMode_clicked(View view){

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();

    }
}
