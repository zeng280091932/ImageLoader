package com.beauney.imageloader.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

/**
 * 图片压缩器
 *
 * @author zengjiantao
 * @since 2020-08-12
 */
public abstract class BitmapDecoder {
    public abstract Bitmap decodeBitmapWithOption(BitmapFactory.Options options) throws IOException;

    /**
     * 压缩图片
     *
     * @param reqWidth  指定要缩放后的宽度
     * @param reqHeight 指定要缩放后的高度
     * @return
     */
    public Bitmap decodeBitmap(int reqWidth, int reqHeight) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //第一次读流只获取宽高配置信息
        decodeBitmapWithOption(options);
        calculateSampleSizeWithOption(options, reqWidth, reqHeight);
        //第二次读流真正获取图片信息
        return decodeBitmapWithOption(options);
    }

    /**
     * 计算图片缩放的比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    private void calculateSampleSizeWithOption(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //计算缩放的比例
        //图片的原始宽高
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            //宽高的缩放比例
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);

            //有的图是长图、有的是宽图
            inSampleSize = Math.max(heightRatio, widthRatio);
        }

        //全景图
        //当inSampleSize为2，图片的宽与高变成原来的1/2
        //options.inSampleSize = 2
        options.inSampleSize = inSampleSize;

        //每个像素2个字节
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        //Bitmap占用内存
        options.inJustDecodeBounds = false;
        //当系统内存不足时可以回收Bitmap
        options.inPurgeable = true;
        options.inInputShareable = true;
    }

}
