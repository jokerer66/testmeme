package com.instanza.testmemo.Adapter;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader.Factory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import java.io.InputStream;

/**
 * Created by apple on 2018/2/26.
 */

public class testokhttpmodule implements GlideModule {
    public testokhttpmodule() {
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new Factory());
    }
}