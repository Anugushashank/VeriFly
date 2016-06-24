package com.example.aparna.buddy.app;

/**
 * Created by Aparna on 05-Apr-16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aparna.buddy.adapter.CardAdapter;
import com.example.aparna.buddy.model.BorrowerData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class TabFragment extends Fragment {
    private String tabData;
    private int taskType;

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.tab_new, container, false);
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        Bundle args = getArguments();

        tabData  = args.getString("tasksData");
        taskType = args.getInt("taskType");
        Type type = new TypeToken<List<BorrowerData>>(){}.getType();
        List<BorrowerData> cardDataList = new Gson().fromJson(tabData, type);

        //((TextView) rootView.findViewById(R.id.textView)).setText(args.getString(ARG_OBJECT));
        setuprecyclerview(rootView, inflater, cardDataList);
        return rootView;
    }

    private void setuprecyclerview(View view, LayoutInflater inflater, List<BorrowerData> cardDataList)
    {
        RecyclerView recylcerView = (RecyclerView)view. findViewById(R.id.recycleView);
        CardAdapter adapter = new CardAdapter(inflater.getContext(), cardDataList, taskType);
        recylcerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(inflater.getContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recylcerView.setLayoutManager(mLinearLayoutManagerVertical);
    }
}

