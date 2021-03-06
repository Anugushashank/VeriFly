package com.interns.verifly.buddy.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.interns.verifly.buddy.app.BorrowerDetailsActivity;
import com.interns.verifly.buddy.app.R;
import com.interns.verifly.buddy.model.BorrowerData;
import com.interns.verifly.buddy.model.BuddyConstants;
import com.google.gson.Gson;

import java.util.List;


/**
 * Created by Aparna on 05-Apr-16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{

    private List<BorrowerData> bData;
    private LayoutInflater bInflator;
    private Context context;
    private int taskType;

    public CardAdapter(Context context, List<BorrowerData> data, int taskType) {
        this.bData     = data;
        this.taskType  = taskType;
        this.bInflator = LayoutInflater.from(context);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = bInflator.inflate(R.layout.list_item, parent, false);
        context = view.getContext();

        CardViewHolder holder = new CardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        BorrowerData currentObj = bData.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        if(bData != null) {
            return bData.size();
        }
        else{
            return 0;
        }
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        int position;
        private TextView name, college, date, tasktype;
        private BorrowerData current;

        public CardViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.borrowerName);
            college = (TextView)itemView.findViewById(R.id.borrowerCollege);
            date = (TextView)itemView.findViewById(R.id.dueDate);
            tasktype = (TextView)itemView.findViewById(R.id.taskType);
            final SharedPreferences settings = context.getSharedPreferences(BuddyConstants.PREFS_FILE, 0);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (taskType != 2) {
                            Bundle bundle = new Bundle();
                            bundle.putString("borrowerData", new Gson().toJson(current));

                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt("tab",taskType);
                            editor.apply();

                            Intent i = new Intent(context, BorrowerDetailsActivity.class);
                            i.putExtras(bundle);
                            context.startActivity(i);
                        }
                    }
                });

        }

        public void setData(BorrowerData currentObj, int position){
            try {
                this.name.setText(currentObj.getUploadDocModel().getName());
            }
            catch(Exception e){
            }
            try {
                this.college.setText(currentObj.getUploadDocModel().getCollege());
            }
            catch(Exception e){
            }
            try {
                this.date.setText(currentObj.getScheduleDate().substring(0, 10));
            }
            catch(Exception e){
            }
            try {
                this.tasktype.setText(currentObj.getTaskType());
            }
            catch(Exception e){
            }
            try {
                this.position = position;
            }
            catch(Exception e){

            }
            try {
                this.current = currentObj;
            }
            catch (Exception e){

            }
        }
    }
}
