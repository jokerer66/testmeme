package com.instanza.testmemo.Adapter;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.instanza.testmemo.Helper.OkHttpHelper;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;


/**
 * Created by apple on 2018/2/12.
 */

public class MyGlideModule implements GlideModule {
    private static String downloadDirectoryPath;
    private static int cacheSize = 100*1000*1000;
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        setDownloadDirectoryPath();
        Log.e("test","path = " + downloadDirectoryPath);
        File file = new File(downloadDirectoryPath);
        if(!file.exists()){
            file.mkdirs();
        }
        //设置缓存的大小为100M
        try{builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));
            Log.e("test","path = create downloadDirectoryPath= "+downloadDirectoryPath );
        }catch (Exception e){
            e.printStackTrace();
            Log.e("test","path = error" );
        }
    }

    private static void setDownloadDirectoryPath(){
        File storageDirectory = Environment.getExternalStorageDirectory();
        downloadDirectoryPath=storageDirectory+"/SOMAaaaa";
    }

    public static String getDownloadDirectoryPath() {
        setDownloadDirectoryPath();
        return downloadDirectoryPath;
    }

    public static int getCacheSize() {
        return cacheSize;
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkHttpHelper.getIntance().getOkHttpClient()));
    }
}