package com.instanza.testmemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instanza.testmemo.Adapter.MyLayoutManager;
import com.instanza.testmemo.R;

/**
 * Created by apple on 2018/3/22.
 */

public class LayoutMaActivity extends AppCompatActivity {
    String[] strs = {"aaa","bbb","ccc","ddd","eee"};
    RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutall);
        recyclerView = (RecyclerView) findViewById(R.id.testrecycler);
        MyLayoutManager myLayoutManager = new MyLayoutManager();
//        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(myLayoutManager);
        MAdapter mAdapter = new MAdapter();
        recyclerView.setAdapter(mAdapter);
    }


    private class MAdapter extends RecyclerView.Adapter<MViewHolder> {
        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(LayoutMaActivity.this).inflate(R.layout.testlayout,parent,false);
            return new MViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            holder.textView.setText(strs[position]);
        }

        @Override
        public int getItemCount() {
            return strs.length;
        }
    }
    private class MViewHolder extends RecyclerView.ViewHolder {
        TextView textView ;
        public MViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.testtest);
        }
    }
}
