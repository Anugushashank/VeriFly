package com.example.aparna.buddy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aparna.buddy.app.BorrowerDetailsActivity;
import com.example.aparna.buddy.app.DetailsActivity;
import com.example.aparna.buddy.app.R;
import com.example.aparna.buddy.model.BorrowerData;
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
        private TextView name, college, date;
        private BorrowerData current;

        public CardViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.borrowerName);
            college = (TextView)itemView.findViewById(R.id.borrowerCollege);
            date = (TextView)itemView.findViewById(R.id.dueDate);


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (taskType != 2) {
                            Bundle bundle = new Bundle();
                            bundle.putString("borrowerData", new Gson().toJson(current));

                            Intent i = new Intent(context, BorrowerDetailsActivity.class);
                            i.putExtras(bundle);
                            context.startActivity(i);
                        }
                    }
                });

        }

        public void setData(BorrowerData currentObj, int position){
            this.name.setText(currentObj.getUploadDocModel().getName());
            this.college.setText(currentObj.getUploadDocModel().getCollege());
            this.date.setText(currentObj.getUploadDocModel().getDate());
            this.position = position;
            this.current = currentObj;
        }
    }
}
