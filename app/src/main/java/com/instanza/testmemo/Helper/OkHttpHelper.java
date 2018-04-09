package com.instanza.testmemo.Helper;

import android.util.Log;

import com.instanza.testmemo.Adapter.MyResponseBody;
import com.instanza.testmemo.Adapter.OKHttpGlideloader;
import com.instanza.testmemo.Adapter.ProcessListen;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apple on 2018/2/28.
 */

public class OkHttpHelper {
    private OkHttpClient okHttpClient;
    private static OkHttpHelper okHttpHelper;
    private static Map<String,ProcessListen> listenMap = new HashMap<>();
    private WeakReference<OkHttpHelper> weakper = new WeakReference<OkHttpHelper>(okHttpHelper);
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
//            Request request2 = request.newBuilder().url("http://i.imgur.com/rLR2cyc.jpg").build();
            Response response = chain.proceed(request);
            Log.e("read","url = "+request.url());
            ProcessListen processListen = listenMap.get(request.url().toString());
            Response response1 = response.newBuilder().body(new MyResponseBody(response.body(), processListen )).build();
            return response1;
        }
    };

    public static OkHttpHelper getIntance() {
        if (okHttpHelper == null) {
            synchronized (OkHttpHelper.class) {
                if (okHttpHelper == null) {
                    okHttpHelper = new OkHttpHelper();
                }
            }
        }
        return okHttpHelper;
    }

    public OkHttpHelper() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.networkInterceptors().add(interceptor);
            okHttpClient = builder.build();
        }
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static void addListener(String url,ProcessListen processListen){
        if(processListen != null)
        synchronized (listenMap){
            if(!listenMap.containsKey(url))
            listenMap.put(url,processListen);
        }

    }

    public static void removeListener(String url){
            synchronized (listenMap){
                if(listenMap.containsKey(url))
                listenMap.remove(url);
            }

    }

    public static ProcessListen findListener(String url) {
        synchronized (listenMap) {
            if(listenMap.containsKey(url))
           return listenMap.get(url);
            else return null;
        }
    }
}
