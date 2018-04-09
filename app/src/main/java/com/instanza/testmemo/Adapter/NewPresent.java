package com.instanza.testmemo.Adapter;

import android.app.Fragment;
import android.util.Log;

import com.bumptech.glide.util.LogTime;
import com.instanza.testmemo.Activity.NewActivity;

import java.lang.ref.WeakReference;

/**
 * Created by apple on 2018/2/24.
 */

public class NewPresent {
    static final String TAG = "NewPresent";
    Object object = new Object();
    WeakReference<NewActivity> weakProductA ;
    public NewPresent(NewActivity newActivity) {
        this.weakProductA = new WeakReference<>(newActivity);
    }

    public void login(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(8*1000);

                }catch (Exception e){
                    e.printStackTrace();
                }

                if(weakProductA.get() == null){
                    Log.e(TAG,"newActivity == null");
                }else{
                    Log.e(TAG,"newActivity != null");
                }
                if(NewPresent.this == null){
                    Log.e(TAG,"NewPresent == null");
                }else{
                    Log.e(TAG,"NewPresent != null");
                }
            }
        }).start();

    }

    public void login2(){
        if(weakProductA.get() == null){
            Log.e(TAG,"login2 newActivity == null");
        }else{
            Log.e(TAG,"login2 newActivity != null");
        }
        if(NewPresent.this == null){
            Log.e(TAG,"login2 NewPresent == null");
        }else{
            Log.e(TAG,"login2 NewPresent != null");
        }
        weakProductA.get().finish();
        if(weakProductA.get() == null){
            Log.e(TAG,"login2 newActivity == null");
        }else{
            Log.e(TAG,"login2 newActivity != null");
        }
        if(NewPresent.this == null){
            Log.e(TAG,"login2 NewPresent == null");
        }else{
            Log.e(TAG,"login2 NewPresent != null");
        }
    }
}
