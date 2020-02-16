package yoyo.jassie.labtest1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import yoyo.jassie.labtest1.Room.Task;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.TasksViewHolder> {

    private Context mCtx;
    private List<Task> taskList;

    public RecyclerAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_view, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.textViewNameRV.setText(t.getName());
        holder.textViewAgeRV.setText(""+t.getAge());
        holder.textViewTuitionRV.setText(""+t.getTuition());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
        String dateTime = dateFormat.format(t.getDate());
        holder.textViewDateRV.setText(""+dateTime);

        animate(holder);

    }

    public void animate(RecyclerView.ViewHolder viewHolder) {

        Animation animAnticipateOvershoot = animAnticipateOvershoot = AnimationUtils.loadAnimation(mCtx, R.anim.slide);

        viewHolder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewNameRV, textViewAgeRV, textViewTuitionRV, textViewDateRV;

        public TasksViewHolder(View itemView) {
            super(itemView);

            textViewNameRV = itemView.findViewById(R.id.textViewNameRecycler);
            textViewAgeRV = itemView.findViewById(R.id.textViewAgeRecycler);
            textViewTuitionRV = itemView.findViewById(R.id.textViewTuitionRecycler);
            textViewDateRV = itemView.findViewById(R.id.textViewDateRecycler);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());

            Intent intent = new Intent(mCtx, Update.class);
            intent.putExtra("task", task);
            mCtx.startActivity(intent);
        }
    }
}