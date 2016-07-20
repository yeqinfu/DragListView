package com.ppandroid.draglistview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import draglist.AD_DragBase;
import draglist.DragListView;

public class MainActivity extends AppCompatActivity {

    private DragListView drag_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drag_list = (DragListView) findViewById(R.id.drag_list);
        List<String> list=new ArrayList<>();
        for (int i=0;i<30;i++){
            list.add("test+"+i);
        }
        MyAdapter adapter=new MyAdapter(this);
        adapter.setDatas(list);
        drag_list.setAdapter(adapter);
    }

    class MyAdapter extends AD_DragBase<String> {

        LayoutInflater inflater;
        public MyAdapter(Context context) {
            super(context);
            inflater=LayoutInflater.from(context);
        }

        @Override
        public View initItemView(int position, View convertView, ViewGroup parent) {
            View item=inflater.inflate(R.layout.item,null);
            TextView tv= (TextView) item.findViewById(android.R.id.text1);
            tv.setText(ts.get(position));
            return item;
        }
    }
}
