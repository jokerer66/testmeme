package com.instanza.testmemo.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

/**
 * Created by apple on 2017/12/25.
 */

public class ImageUtil {
    private static final int UNCONSTRAINED = -1;


    public static Bitmap fileToBitmapThumb(String filePath, int weight,
                                           int height) throws Exception {
        if (null == filePath) {
            return null;
        }
        int targetSize = Math.min(weight, height);
        int maxPixels = weight * height;

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        FileDescriptor fd = fis.getFD();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFileDescriptor(fd, null, options);

        if (options.mCancel || options.outWidth == -1
                || options.outHeight == -1) {
            fis.close();
            throw new Exception("invalid bitmap file");
        }
        int sampleSize = computeSampleSize(options, targetSize, maxPixels);
        int maxSample = Math.max(sampleSize, 20);
        options.inJustDecodeBounds = false;

        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        for (int index = sampleSize; index <= maxSample; index++) {
            try {
                options.inSampleSize = index;
                Bitmap bm = BitmapFactory.decodeFileDescriptor(fd, null,
                        options);
                if (null != bm) {
                    fis.close();
                    return bm;
                }
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            } catch (Throwable t) {
            }
        }
        fis.close();
        throw new Exception("decodeFile Failed");

    }


    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        roundedSize = roundedSize < 1 ? 1 : roundedSize;//SampleSize<1时等同于1，且当SampleSize<1时会导致bug，所以限制其最小值为1.
        return roundedSize;
    }


    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
                .ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
                .min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == UNCONSTRAINED)
                && (minSideLength == UNCONSTRAINED)) {
            return 1;
        } else if (minSideLength == UNCONSTRAINED) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
