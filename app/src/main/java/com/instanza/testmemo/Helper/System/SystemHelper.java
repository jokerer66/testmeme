package com.instanza.testmemo.Helper.System;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by apple on 2017/10/11.
 */

public class SystemHelper {
    private Context context;
    private static SystemHelper systemHelper;
    public static SystemHelper getInstance(){
        if(systemHelper == null){
            synchronized (SystemHelper.class){
                systemHelper = new SystemHelper();
            }

        }
        return systemHelper;
    }
    public static SystemHelper getInstance(Context context){
        if(systemHelper == null){
            synchronized (SystemHelper.class){
                systemHelper = new SystemHelper(context);
            }

        }
        return systemHelper;
    }

    public SystemHelper() {
    }

    public SystemHelper(Context context) {
        this.context = context;
    }

    public void hideIME(EditText edit, boolean clearFocus) {
        if (edit == null) {
            return;
        }
        if (clearFocus) {
            edit.clearFocus();
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }
}
