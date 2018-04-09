package com.instanza.testmemo.Adapter;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by apple on 2018/3/7.
 */

public class MyRequestBody extends RequestBody {
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {

    }
}
