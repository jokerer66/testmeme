package com.instanza.testmemo.Activity.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by apple on 2018/4/4.
 */

public class MyServer extends Service {
    IBinder iBinder = new IMyAidlInterface.Stub() {
        @Override
        public void addperson() throws RemoteException {

        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }


    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
