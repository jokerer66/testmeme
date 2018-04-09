package com.instanza.testmemo.Helper;

import android.os.Environment;
import android.os.Handler;

import com.instanza.testmemo.Activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by apple on 2017/12/22.
 */

public class httprequest implements Runnable {

    String path;
    urlCallback callback;
    public httprequest(String path,urlCallback callback) {
        this.path = path;
        this.callback = callback;
    }
    public interface urlCallback{
        void docallback(String path);
    }
    @Override
    public void run() {
        saveImageToDisk(path);
    }

    public void saveImageToDisk(String path)
    {
        InputStream inputStream= getInputStream(path);
        byte[] data=new byte[1024];
        int len=0;
        FileOutputStream fileOutputStream=null;
        try {
            //把图片文件保存在本地下
            String path2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/jksaigou/cache/";
            File file = new File(path2);
            if(file.exists()){

            }else{
                file.mkdirs();
            }
            fileOutputStream=new FileOutputStream(new File(path2+"rrrr"));

            while(true)
            {
                int leng = inputStream.read(data);
                if(leng == -1){
                    break;
                }
                //向本地文件中写入图片流
                fileOutputStream.write(data,0,leng);
            }
            callback.docallback(path2+"rrrr");
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            //最后关闭流
            if(inputStream!=null)
            {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!=null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static InputStream getInputStream(String urlstr){
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            //根据URL地址实例化一个URL对象，用于创建HttpURLConnection对象。
            URL url = new URL(urlstr);

            if (url != null) {
                //openConnection获得当前URL的连接
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //设置3秒的响应超时
                httpURLConnection.setConnectTimeout(10000);
                //设置允许输入
                httpURLConnection.setDoInput(true);
                //设置为GET方式请求数据
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                //获取连接响应码，200为成功，如果为其他，均表示有问题
                int responseCode=httpURLConnection.getResponseCode();
                if(responseCode==200)
                {
                    //getInputStream获取服务端返回的数据流。
                    inputStream=httpURLConnection.getInputStream();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public static boolean isSDCardAvailable() {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
