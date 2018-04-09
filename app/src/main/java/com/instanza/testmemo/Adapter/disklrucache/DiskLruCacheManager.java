package com.instanza.testmemo.Adapter.disklrucache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.instanza.testmemo.Activity.MyApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by apple on 2018/3/1.
 */

public class DiskLruCacheManager {
    private static DiskLruCacheManager diskManager;
    private Context context;
    private DiskLruCache diskLruCache;

    public DiskLruCacheManager() {
        try {
            this.context = MyApplication.getAppContext();
            File cacheDir = getDiskCacheDir(context, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskLruCacheManager getIntance(){
        if(diskManager == null){
            synchronized (DiskLruCacheManager.class){
                if(diskManager == null){
                    diskManager = new DiskLruCacheManager();
                }
            }
        }
        return diskManager;
    }

    public DiskLruCache getDiskLruCache() {
        return diskLruCache;
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public InputStream readFromDisk(String key){
        InputStream inputStream = null;
        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = diskLruCache.get(key);
            if (snapShot != null) {
                inputStream = snapShot.getInputStream(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public void writeToDisk(String key, InputStream inputStream){
        try{
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if(editor != null && inputStream != null){
                if(writeToDisk(editor.newOutputStream(0),inputStream)){
                    editor.commit();
                    Log.e("disk", "edit commit");
                }else{
                    editor.abort();
                    Log.e("disk", "edit abort");
                }
                diskLruCache.flush();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean writeToDisk(OutputStream outputStream, InputStream inputStream){
        boolean flag = false;
        try{
            byte[] data = new byte[1024*10];
            int len = -1;
            while ((len = inputStream.read(data)) != -1){
                outputStream.write(data,0,len);
            }
            outputStream.flush();
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e("tag3","key flag = "+flag);
        return flag;
    }

}
