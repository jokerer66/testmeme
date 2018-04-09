package com.instanza.testmemo.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instanza.testmemo.R;

/**
 * Created by apple on 2017/10/10.
 */

public class SettingFragment extends Fragment {

    public static final String TAG = "content1";
    private View view;
    private TextView textView;
    private String content;

    public static SettingFragment newInstance(String content) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(TAG, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        content = bundle != null ? bundle.getString(TAG) : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting, container, false);
//        init();
        return view;
    }

//    private void init() {
//        textView = (TextView) view.findViewById(R.id.textView1);
//        textView.setText(content);
//    }
}
