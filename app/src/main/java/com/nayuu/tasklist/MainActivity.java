package com.nayuu.tasklist;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nayuu.tasklist.orm.Task;
import com.nayuu.tasklist.orm.TaskDatabase;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText startTimeEditText;
    private int year, month, day;
    private Button taskConfirm;
    private EditText endTimeEditText;
    private NumberPicker timeNumberPicker;
    private EditText taskNameEditText;
    private RecyclerView taskRecycleView;
    private int time = -1;
    private Task task = null;

    private List<Task> taskList = null;
    private static final int INSERT_COMPLETE = 5;
    private static final int SELECT_COMPLETE = 6;
    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == INSERT_COMPLETE && taskList != null) {
                TaskListAdapter taskListAdapter = new TaskListAdapter(MainActivity.this, taskList, adapterInterface);
                taskRecycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                taskRecycleView.setAdapter(taskListAdapter);
                //reset
                startTimeEditText.setText("");
                endTimeEditText.setText("");
                timeNumberPicker.setValue(1);
                taskNameEditText.setText("");
                time = -1;
                task = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initDataBase();

    }

    private void initUI() {
        startTimeEditText = (EditText) findViewById(R.id.start_time_edit_text);
        endTimeEditText = (EditText) findViewById(R.id.end_time_edit_text);
        timeNumberPicker = (NumberPicker) findViewById(R.id.time_number_picker);
        taskNameEditText = (EditText) findViewById(R.id.task_name_edit_text);
        taskConfirm = (Button) findViewById(R.id.task_confirm);
        taskRecycleView = (RecyclerView) findViewById(R.id.task_recycle_view);

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startTimeEditText.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                dpd.show();
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        endTimeEditText.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        timeNumberPicker.setMinValue(1);
        timeNumberPicker.setMaxValue(55);
        timeNumberPicker.setWrapSelectorWheel(false);
        timeNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        timeNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                time = newVal;

            }
        });
        taskConfirm.setOnClickListener(confirmButton);
    }

    private void initDataBase() {
        new Thread(selectDatabase).start();
    }

    private View.OnClickListener confirmButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //防錯
            if (taskNameEditText.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "填寫未完成", Toast.LENGTH_SHORT).show();
                return;
            }

            if (startTimeEditText.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "填寫未完成", Toast.LENGTH_SHORT).show();
                return;
            }

            if (endTimeEditText.getText().toString().equals("")) {
                Toast.makeText(MainActivity.this, "填寫未完成", Toast.LENGTH_SHORT).show();
                return;
            }

            if (time == -1) {
                Toast.makeText(MainActivity.this, "填寫未完成", Toast.LENGTH_SHORT).show();
                return;
            }

            task = new Task();
            task.setSort("");
            task.setTaskName(taskNameEditText.getText().toString());
            task.setStartTime(startTimeEditText.getText().toString());
            task.setEndTime(endTimeEditText.getText().toString());
            task.setSpendTime(time);

            //使用room存取 SQLLite 只能用Thread Handle, 無法使用AsyncTask
            new Thread(insertAndSelectDatabase).start();
        }
    };

    private Runnable insertAndSelectDatabase = new Runnable() {
        @Override
        public void run() {
            TaskDatabase.getInstance(MainActivity.this).getTaskDao().insert(task);
            handle.sendEmptyMessage(INSERT_COMPLETE);

            taskList = TaskDatabase.getInstance(MainActivity.this).getTaskDao().getAllTask();
        }
    };

    private Runnable selectDatabase = new Runnable() {
        @Override
        public void run() {
            taskList = TaskDatabase.getInstance(MainActivity.this).getTaskDao().getAllTask();
            handle.sendEmptyMessage(INSERT_COMPLETE);
        }
    };

    private AdapterInterface adapterInterface = new AdapterInterface() {
        @Override
        public void adapterCheckBoxClick(final String taskName) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TaskDatabase.getInstance(MainActivity.this).getTaskDao().deleteTask(taskName);
                    taskList = TaskDatabase.getInstance(MainActivity.this).getTaskDao().getAllTask();
                    handle.sendEmptyMessage(INSERT_COMPLETE);
                }
            }).start();
        }
    };
}
