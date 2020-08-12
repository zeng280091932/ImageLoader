package com.beauney.imageloader.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beauney.imageloader.request.BitmapRequest;
import com.beauney.imageloader.utils.BitmapDecoder;
import com.beauney.imageloader.utils.ImageViewHelper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public class UrlLoader extends AbsLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) new URL(request.getImageUri()).openConnection();
            //mark与reset支持重复使用流，但是InputStream不支持
            inputStream = new BufferedInputStream(connection.getInputStream());

            //标记
            inputStream.mark(inputStream.available());

            final InputStream finalInputStream = inputStream;
            BitmapDecoder bitmapDecoder = new BitmapDecoder() {
                @Override
                public Bitmap decodeBitmapWithOption(BitmapFactory.Options options) throws IOException {
                    Bitmap bitmap = BitmapFactory.decodeStream(finalInputStream, null, options);
                    if (options.inJustDecodeBounds) {
                        //重置
                        //则输入流总会在调用 mark 之后记住所有读取的字节，并且无论何时调用方法 reset ，都会准备再次提供那些相同的字节
                        //但是，如果在调用 reset 之前可以从流中读取多于 readlimit 的字节，则根本不需要该流记住任何数据。
                        finalInputStream.reset();

                    }
                    return bitmap;
                }
            };
            return bitmapDecoder.decodeBitmap(ImageViewHelper.getImageViewWidth(request.getImageView()),
                    ImageViewHelper.getImageViewHeight(request.getImageView()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
                connection = null;
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
