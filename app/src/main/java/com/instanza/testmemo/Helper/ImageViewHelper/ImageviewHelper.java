package com.instanza.testmemo.Helper.ImageViewHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.webkit.DownloadListener;
import android.widget.ImageView;

import com.instanza.testmemo.Activity.MyApplication;
import com.instanza.testmemo.Adapter.disklrucache.DiskLruCacheManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2018/3/16.
 */

public class ImageviewHelper {
    private Bitmap mBitmap = null;
    private DiskLruCacheManager diskLruCacheManager;
    private static ImageviewHelper imageviewHelper;
    private LruCache<String, Bitmap> mMemoryCache;
    private Map<String,DownLoadTask> downLoadTaskMap ;

    private static ImageviewHelper getInstance(){
        if(imageviewHelper == null){
            synchronized (ImageviewHelper.class){
                if(imageviewHelper == null){
                    imageviewHelper = new ImageviewHelper();
                }
            }
        }
        return imageviewHelper;
    }

    public ImageviewHelper() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

        diskLruCacheManager = DiskLruCacheManager.getIntance();
        downLoadTaskMap = new HashMap<>();
    }

    public void loadImage(String imageUrl, ImageView imageView){
        DownLoadTask downLoadTask = new DownLoadTask();
        downLoadTask.execute(imageUrl);
    }

    private class DownLoadTask extends AsyncTask<String,Long,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downLoadImage(params[0]);
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                mBitmap = bitmap;
            }
        }

        protected Bitmap downLoadImage(String urlPath){

            String key = null;
            Bitmap bitmap = null;
            InputStream inputStream = null;

            try{

                key = hashKeyForDisk(urlPath);
                inputStream = diskLruCacheManager.readFromDisk(key);
                if(inputStream == null){
                    network(urlPath,key);
                }
                inputStream = diskLruCacheManager.readFromDisk(key);

                if (inputStream != null) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
                if (bitmap != null) {
                    // 将Bitmap对象添加到内存缓存当中
                    addBitmapToMemoryCache(urlPath, bitmap);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void network(String urlPath,String key){
            URL url;
            HttpURLConnection connection = null;
            try{
                url = new URL(urlPath);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    diskLruCacheManager.writeToDisk(key,connection.getInputStream());
                    Log.e("tag3","download key = "+key);
                }

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }


    public void addDownloadTask(String imageUrl, DownLoadTask downLoadTask){
        if(imageUrl == null || downLoadTask == null){
            return;
        }
        if(downLoadTaskMap == null){
            downLoadTaskMap = new HashMap<>();
        }
        downLoadTaskMap.put(imageUrl,downLoadTask);

    }

    public void removeDownloadTask(String imageUrl){
        if(imageUrl == null ){
            return;
        }
        if(downLoadTaskMap == null){
            return;
        }
        if(downLoadTaskMap.containsKey(imageUrl))
        downLoadTaskMap.remove(imageUrl);

    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param bitmap
     *            LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
