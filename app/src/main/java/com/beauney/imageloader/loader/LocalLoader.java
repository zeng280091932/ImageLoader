package com.beauney.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.beauney.imageloader.request.BitmapRequest;
import com.beauney.imageloader.utils.BitmapDecoder;
import com.beauney.imageloader.utils.ImageViewHelper;

import java.io.File;
import java.io.IOException;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public class LocalLoader extends AbsLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        final String path = Uri.parse(request.getImageUri()).getPath();
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        BitmapDecoder decoder = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(path, options);
            }
        };
        return decoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),
                ImageViewHelper.getImageViewHeight(request.getImageView()));
    }
}
