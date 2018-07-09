package com.hour24.parallaximageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ViewAdapter mAdapter;

    private ArrayList<Model> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ViewAdapter(this, getList());
        mRecyclerView.setAdapter(mAdapter);

    }

    public ArrayList<Model> getList() {

        mList = new ArrayList<>();

        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_1_993.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_6_987.jpg")));
        mList.add((new Model(0, "http://tourimage.interpark.com/product/tour/00161/A30/500/A3014824_7_533.jpg")));

        return mList;
    }
}
