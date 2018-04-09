package com.instanza.testmemo.Activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by apple on 2017/10/19.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
//        for(int i=0;i<20;i++){
//            try{
//                Thread.sleep(1000);
//                System.out.println("i = "+i);
//            }catch (Exception e){
//
//            }
//        }
        context = getApplicationContext();
        System.out.println("myapplication oncreate");
    }

    public static Context getAppContext(){
        return context;
    }


}
