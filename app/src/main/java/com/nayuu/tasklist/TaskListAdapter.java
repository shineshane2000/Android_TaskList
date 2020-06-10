package com.nayuu.tasklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nayuu.tasklist.orm.Task;

import java.util.List;

/**
 * Created by Akatsuki on 2020/6/3 10:54.
 */
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;
    private AdapterInterface adapterInterface;

    public TaskListAdapter(Context context, List<Task> taskList, AdapterInterface adapterInterface) {
        this.context = context;
        this.taskList = taskList;
        this.adapterInterface = adapterInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_list_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.completeCheckBox = (CheckBox) view.findViewById(R.id.complete_checkBox);
        viewHolder.taskEndTime = (TextView) view.findViewById(R.id.task_end_time);
        viewHolder.taskStartTime = (TextView) view.findViewById(R.id.task_start_time);
        viewHolder.taskName = (TextView) view.findViewById(R.id.task_name);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Task task = taskList.get(position);

        viewHolder.taskName.setText(task.getTaskName());
        viewHolder.taskStartTime.setText(task.getStartTime());
        viewHolder.taskEndTime.setText(task.getEndTime());

        viewHolder.completeCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapterInterface.adapterCheckBoxClick(task.getTaskName());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView taskName;
        public TextView taskStartTime;
        public TextView taskEndTime;
        public CheckBox completeCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
