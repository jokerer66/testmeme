package com.instanza.testmemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.instanza.testmemo.Adapter.NewPresent;
import com.instanza.testmemo.R;

import static com.instanza.testmemo.Activity.MainActivity.present;

/**
 * Created by apple on 2018/2/24.
 */

public class NewActivity extends AppCompatActivity{
    private NewPresent newPresent;
    Button newButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newlayout);
        newButton = (Button) findViewById(R.id.newbutton);
        newPresent = new NewPresent(this);
//        newButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                newPresent.login();
//            }
//        });
    }

    public void showToast(){
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(newPresent == null){
            Log.e("NewPresent2","NewPresent == null");
        }else{
            Log.e("NewPresent2","NewPresent != null");
        }
    }
}
