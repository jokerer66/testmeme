package com.instanza.testmemo.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.EmptySignature;
import com.instanza.testmemo.Adapter.MyGlideModule;
import com.instanza.testmemo.Adapter.MyLayoutManager;
import com.instanza.testmemo.Adapter.PhotoWallAdapter;
import com.instanza.testmemo.Adapter.ProcessListen;
import com.instanza.testmemo.Adapter.disklrucache.DiskLruCache;
import com.instanza.testmemo.Adapter.disklrucache.DiskLruCacheManager;
import com.instanza.testmemo.Helper.OkHttpHelper;
import com.instanza.testmemo.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by apple on 2018/2/8.
 */

public class SimplePhotoActivity extends AppCompatActivity {
    String imageUrl = "http://img.blog.csdn.net/20140803100719140?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ3VvbGluX2Jsb2c=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast";
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, Bitmap> mMemoryCache;
    /**
     * 记录所有正在下载或等待下载的任务。
     */
//    private Set<BitmapWorkerTask> taskCollection;
    private static final String TAG = "SimplePhotoActivity";
    private ImageView photoView;
    private PhotoView photoView2;
    private RecyclerView photowall;
    private MyAdapter myAdapter;
    private DiskLruCacheManager diskLruCacheManager;
    private Handler handler = new Handler(Looper.getMainLooper());
    public static String[] eatFoodyImages = {
//            "http://pic.element3ds.com/forum/201608/15/232854s72fd1o19bgjpj1b.png",
//            "http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            photoView.setImageBitmap( bitmap );
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_wall2);
//        taskCollection = new HashSet<BitmapWorkerTask>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

        diskLruCacheManager = DiskLruCacheManager.getIntance();
        photowall = (RecyclerView) findViewById(R.id.photowall2);
//        photoView = (ImageView) findViewById(R.id.phototest);
////        photoView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.testimage2));
//        photoView2 = (PhotoView) findViewById(R.id.imgs);
//        loadBitmaps(photoView,imageUrl);



//        setPhotoView("http://pic.element3ds.com/forum/201608/15/232854s72fd1o19bgjpj1b.png");
//        Glide.with(SimplePhotoActivity.this)
//                .load("http://pic.element3ds.com/forum/201608/15/232854s72fd1o19bgjpj1b.png")
//                .placeholder(R.drawable.pic_infor_default)
//                .centerCrop()
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//
//                        return false;
//                    }
//                })
//                .into(photoView);
//
//        photoView2.setImageResource(R.drawable.testimage2);
        MyLayoutManager myLayoutManager = new MyLayoutManager();
//        LinearLayoutManager myLayoutManager = new LinearLayoutManager(SimplePhotoActivity.this,LinearLayoutManager.VERTICAL,false);
        photowall.setLayoutManager(myLayoutManager);
//        photowall.setLayoutManager(new LinearLayoutManager(SimplePhotoActivity.this,LinearLayoutManager.VERTICAL,false));
//        photowall.setLayoutManager(new StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL));
        myAdapter = new MyAdapter();
        photowall.setAdapter(myAdapter);
//        photowall.setNestedScrollingEnabled(false);
//        doSynchttpRequest();

    }

    public void setPhotoView(String str){
        Glide.with(SimplePhotoActivity.this)
                .load(str)
                .placeholder(R.drawable.pic_infor_default)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(photoView2);
    }

    private void doSynchttpRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Interceptor interceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.url();
                            String s = url.url().toString();
                            //---------请求之前-----
                            Log.d(TAG,"app interceptor:begin \n"+request.headers());
                            Response  response = chain.proceed(request);
                            Log.d(TAG,"app interceptor:end \n"+response.headers());
                            //---------请求之后------------
                            return response;
                        }
                    };

                    Interceptor netinterceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            //---------请求之前-----
                            Log.d(TAG,"network interceptor:begin \n"+request.headers());
                            Response  response = chain.proceed(request);
                            Log.d(TAG,"network interceptor:end \n"+response.headers());
                            return response;
                        }
                    };
                    Request request = new Request.Builder().url("http://www.publicobject.com/helloworld.txt").build();
                    RequestBody requestBody = new FormBody.Builder().add("wer","we").build();
                    Request request2 = new Request.Builder().url("http://www.baidu.com").post(requestBody).build();

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.addInterceptor(interceptor);
                    builder.addNetworkInterceptor(netinterceptor);

                    OkHttpClient client = builder.build();
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//                            Log.e(TAG,"code = "+response.code());
//                        }
//                    });
                    Response response = client.newCall(request).execute();//得到Response 对象
                    Log.e(TAG,"code = "+response.code());
                    response.body().byteStream();
//                    if (response.isSuccessful()) {
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                setPhotoView("http://pic.element3ds.com/forum/201608/15/232854s72fd1o19bgjpj1b.png");
//                            }
//                        });
//                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(SimplePhotoActivity.this).inflate(R.layout.simple_photo,parent,false);
            View view = LayoutInflater.from(SimplePhotoActivity.this).inflate(R.layout.simple_photo,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,final int position) {
//            ProcessListen processListen = new ProcessListen() {
//                @Override
//                public void update(long count, long allcount, boolean finished) {
//                    //update process
////                    if(eatFoodyImages[position].equals("http://i.imgur.com/syELajx.jpg")){
//                        Log.e("read","count = "+count+" allcount = "+ allcount);
////                    }
//                    int percent = (int) ((100L * count) / allcount);
//                    holder.progressBar.setProgress(percent);
//                    holder.progressBar.setMax(100);
//                    if(percent == 100){
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                holder.progressBar.setVisibility(View.GONE);
//                                OkHttpHelper.removeListener(eatFoodyImages[position]);
//                            }
//                        });
//
//                    }
//                }
//            };
//            OkHttpHelper.addListener(eatFoodyImages[position],processListen);



            holder.textView.setText("View.GONE");
            holder.progressBar.setVisibility(View.GONE);

            holder.photo.setImageResource(R.drawable.pic_infor_default);
            final Intent intent = new Intent();
            intent.setClass(SimplePhotoActivity.this,FullPhotoActivity.class);
            intent.putExtra("strurls",eatFoodyImages);
            intent.putExtra("selectNumber",position);
            holder.photo.setTag(eatFoodyImages[position]);
//            Log.e("test","position = "+position);
////            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
////                @Override
////                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
////                    int width = bitmap.getWidth();
////                    int height = bitmap.getHeight();
////                    holder.photo.setImageBitmap(bitmap);
////                }
////            };

            File file = loadCacheImage(SimplePhotoActivity.this,eatFoodyImages[position],null,1100,100);
            if(file == null){
                Glide.with(SimplePhotoActivity.this)
                        .load(eatFoodyImages[position]).asBitmap().centerCrop()
                        .placeholder(R.drawable.pic_infor_default)

                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                            OkHttpHelper.removeListener(eatFoodyImages[position]);
//                                e.printStackTrace();
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.progressBar.setVisibility(View.GONE);
//                                    OkHttpHelper.removeListener(eatFoodyImages[position]);
                                    }
                                });
                                return false;
                            }
                        })
//                    .into(holder.photo);
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                int width = bitmap.getWidth();
                                int height = bitmap.getHeight();
                                holder.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                if(holder.photo.getTag().equals(eatFoodyImages[position])){
                                    holder.photo.setImageBitmap(bitmap);
                                }

                            }
                        });

            }else {
                Glide.with(SimplePhotoActivity.this).load(file).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        holder.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        if(holder.photo.getTag().equals(eatFoodyImages[position])){
                            holder.photo.setImageBitmap(bitmap);
                        }

                    }
                });
            }
//
//
//
            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(intent);
                }
            });

//            holder.photo.setTag(eatFoodyImages[position]);
//            LoadImgeTask loadImgeTask = new LoadImgeTask();
//            loadImgeTask.execute(eatFoodyImages[position]);

        }

        public File loadCacheImage(Context context, String url, ImageView imageView, int widthPixels, int heightPixels) {
            // 寻找缓存图片
//            return DiskLruCacheWrapper.get(Glide.getPhotoCacheDir(context), 250 * 1024 * 1024).get(new OriginalKey(url, EmptySignature.obtain()));
            File file = new File(MyGlideModule.getDownloadDirectoryPath());
            if(!file.exists()){
                file.mkdirs();
            }
            DiskCache diskCache = DiskLruCacheWrapper.get(file, MyGlideModule.getCacheSize());
            return diskCache.get(new OriginalKey(url, EmptySignature.obtain()));
        }

        class LoadImgeTask extends AsyncTask<String ,Void,Bitmap>{
            private String imageUrl;

            @Override
            protected Bitmap doInBackground(String... params) {
                imageUrl = params[0];
                return loadImage(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                ImageView imageView = (ImageView) photowall.findViewWithTag(imageUrl);
                if (imageView != null && bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

            private Bitmap loadImage(String urlpath){
                URL url;
                HttpURLConnection connection = null;
                Bitmap bitmap = null;
                try{
                    url = new URL(urlpath);
                    connection = (HttpURLConnection) url.openConnection();
                    bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return bitmap;
            }
        }

        @Override
        public int getItemCount() {
            return eatFoodyImages.length;
        }


    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        ProgressBar progressBar;
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.dsfda);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            photo = (ImageView) itemView.findViewById(R.id.simplephoto);
        }
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
     * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
     * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
     */
//    public void loadBitmaps(ImageView imageView, String imageUrl) {
//        try {
//            Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
//            if (bitmap == null) {
//                BitmapWorkerTask task = new BitmapWorkerTask();
//                taskCollection.add(task);
//                task.execute(imageUrl);
//            } else {
//                if (imageView != null && bitmap != null) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
//    public void cancelAllTasks() {
//        if (taskCollection != null) {
//            for (BitmapWorkerTask task : taskCollection) {
//                task.cancel(false);
//            }
//        }
//    }



    /**
     * 将缓存记录同步到journal文件中。
     */
//    public void fluchCache() {
//        if (mDiskLruCache != null) {
//            try {
//                mDiskLruCache.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }



    /**
     * 异步下载图片的任务。
     *
     * @author guolin
     */
//    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
//
//        /**
//         * 图片的URL地址
//         */
//        private String imageUrl;
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            imageUrl = params[0];
//            InputStream inputStream = null;
//            FileDescriptor fileDescriptor = null;
//            FileInputStream fileInputStream = null;
//            DiskLruCache.Snapshot snapShot = null;
//            try {
//                // 生成图片URL对应的key
//                final String key = hashKeyForDisk(imageUrl);
//
//
//
//                // 查找key对应的缓存
//                snapShot = diskLruCacheManager.getDiskLruCache().get(key);
//                if (snapShot == null) {
//                    // 如果没有找到对应的缓存，则准备从网络上请求数据，并写入缓存
//                    DiskLruCache.Editor editor = diskLruCacheManager.getDiskLruCache().edit(key);
//                    if (editor != null) {
//                        OutputStream outputStream = editor.newOutputStream(0);
//                        if (downloadUrlToStream(imageUrl, outputStream)) {
//                            editor.commit();
//                        } else {
//                            editor.abort();
//                        }
//                    }
//                    // 缓存被写入后，再次查找key对应的缓存
//                    snapShot = diskLruCacheManager.getDiskLruCache().get(key);
//                }
//                if (snapShot != null) {
//                    fileInputStream = (FileInputStream) snapShot.getInputStream(0);
//                    fileDescriptor = fileInputStream.getFD();
//                }
//                // 将缓存数据解析成Bitmap对象
//                Bitmap bitmap = null;
//                if (fileDescriptor != null) {
//                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//                }
//                if (bitmap != null) {
//                    // 将Bitmap对象添加到内存缓存当中
//                    addBitmapToMemoryCache(params[0], bitmap);
//                }
//                return bitmap;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (fileDescriptor == null && fileInputStream != null) {
//                    try {
//                        fileInputStream.close();
//                    } catch (IOException e) {
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            // 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
////            ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
//            if (photoView != null && bitmap != null) {
//                photoView.setImageBitmap(bitmap);
//            }
//            taskCollection.remove(this);
//        }
//
//        /**
//         * 建立HTTP请求，并获取Bitmap对象。
//         *
//         * @param
//         * @return 解析后的Bitmap对象
//         */
//        private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
//            HttpURLConnection urlConnection = null;
//            BufferedOutputStream out = null;
//            BufferedInputStream in = null;
//            try {
//                final URL url = new URL(urlString);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
//                out = new BufferedOutputStream(outputStream, 8 * 1024);
//                int b;
//                while ((b = in.read()) != -1) {
//                    out.write(b);
//                }
//                return true;
//            } catch (final IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                try {
//                    if (out != null) {
//                        out.close();
//                    }
//                    if (in != null) {
//                        in.close();
//                    }
//                } catch (final IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return false;
//        }
//
//    }
}
