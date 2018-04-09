package com.instanza.testmemo.Adapter;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by apple on 2018/2/28.
 */

public class MyResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private ProcessListen processListen;

    public MyResponseBody(ResponseBody responseBody, ProcessListen processListen) {
        this.responseBody = responseBody;
        this.processListen = processListen;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            int i=0;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {

                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
//                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
////                Log.e("read","i = "+i++ + " totalBytesRead = "+ totalBytesRead + " responseBody.contentLength() = "+ responseBody.contentLength());
//                if(processListen != null){
//                    processListen.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
//                }
                return bytesRead;
            }
        };
    }
}
