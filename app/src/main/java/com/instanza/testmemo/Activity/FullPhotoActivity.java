package com.instanza.testmemo.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.EmptySignature;
import com.instanza.testmemo.Adapter.PhotoViewPager;
import com.instanza.testmemo.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class FullPhotoActivity extends AppCompatActivity {
    PhotoViewPager photoViewPager;
    int selectNumber;
    String[] eatFoodyImages;
    MyViewPagerAdapter myViewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        selectNumber = intent.getIntExtra("selectNumber",1);
        eatFoodyImages = intent.getStringArrayExtra("strurls");
//        intent.get
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_photo);

        photoViewPager = (PhotoViewPager) findViewById(R.id.fullphoto);
        myViewPagerAdapter = new MyViewPagerAdapter();
        photoViewPager.setAdapter(myViewPagerAdapter);
        photoViewPager.setCurrentItem(selectNumber);

//        photoView = (PhotoView) findViewById(R.id.fullphoto);
//        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//            @Override
//            public void onOutsidePhotoTap() {
//
//            }
//
//            @Override
//            public void onPhotoTap(View view, float x, float y) {
//                finish();
//            }
//        });
//        Glide.with(FullPhotoActivity.this)
//                .load(eatFoodyImages[selectNumber])
//                .placeholder(R.drawable.pic_infor_default)
//                .fitCenter()
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        return false;
//                    }
//                })
//                .into(photoView);
    }

    public File loadCacheImage(Context context, String url, ImageView imageView, int widthPixels, int heightPixels) {
        // 寻找缓存图片
        File file = DiskLruCacheWrapper.get(Glide.getPhotoCacheDir(context), 250 * 1024 * 1024).get(new OriginalKey(url, EmptySignature.obtain()));
        if(file == null){
            Log.e("wer","url = "+url+"file = null");
        }else {
            Log.e("wer","url = "+url+"file = "+ file.getAbsolutePath());
        }
        return file;
    }

    private class MyViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return eatFoodyImages.length;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(FullPhotoActivity.this,
                    R.layout.item_photofull,null);
            PhotoView mPhotoView = (PhotoView) view.findViewById(R.id.item_fullphoto);
//            ImageView mPhotoView = (ImageView) view.findViewById(R.id.item_fullphoto);
//            Glide.with(FullPhotoActivity.this)
//                .load("http://i.imgur.com/Z3QjilA.jpg").asBitmap()
//                .placeholder(R.drawable.pic_infor_default)
////                .fitCenter()
//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                        e.printStackTrace();
//                        Log.e("error","loadfail");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        resource.getWidth();
//                        return false;
//                    }
//                })
            //                .into(mPhotoView);

//            Glide.with(FullPhotoActivity.this).load("/data/user/0/com.instanza.testmemo/cache/image_manager_disk_cache/0290453c71a163685b462211b94f756c0823d12b61eb977cc92be51e04326cab.0").into(mPhotoView);
//            getUrlTask gaa = new getUrlTask(FullPhotoActivity.this,mPhotoView);
            try{
//                gaa.execute(eatFoodyImages[position]);
                File file = loadCacheImage(FullPhotoActivity.this,eatFoodyImages[position],mPhotoView,100,100);
                Glide.with(FullPhotoActivity.this).load(file).into(mPhotoView);

            }catch (Exception e){
                e.printStackTrace();
            }


            mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    onBackPressed();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


//        @SuppressLint("StaticFieldLeak")
//        public class getUrlTask extends AsyncTask<String, Void, File> {
//
//            private final Context context;
//            private final PhotoView mPhotoView;
//            private getUrlTask(Context context,PhotoView mPhotoView) {
//                this.context = context;
//                this.mPhotoView = mPhotoView;
//            }
//
//            @Override
//            protected File doInBackground(String... params) {
//                String imgUrl =  params[0];
//                try {
//                    return Glide.with(context)
//                            .load(imgUrl)
//                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                            .get();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(File result) {
//                if (result == null) {
//                    return;
//                }
////            //此path就是对应文件的缓存路径
////            String path = result.getPath();
////            //将缓存文件copy, 命名为图片格式文件
////            copyFile(path, newPath);
//                Glide.with(FullPhotoActivity.this).load("/data/user/0/com.instanza.testmemo/cache/image_manager_disk_cache/0290453c71a163685b462211b94f756c0823d12b61eb977cc92be51e04326cab.0").into(mPhotoView);
//
//            }
//        }
    }




}
