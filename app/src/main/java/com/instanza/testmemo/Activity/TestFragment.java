package com.instanza.testmemo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.instanza.testmemo.R;

/**
 * Created by apple on 2018/3/5.
 */

public class TestFragment extends Fragment {
    TextView textView;
    Context mainactivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = LayoutInflater.from(mainactivity).inflate(R.layout.testfragment,container,false);
        textView = (Button) view.findViewById(R.id.testbutton);
        return view;
    }
}
