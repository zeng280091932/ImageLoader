package com.beauney.imageloader.request;

import android.util.Log;

import com.beauney.imageloader.loader.Loader;

import java.util.concurrent.BlockingQueue;

import com.beauney.imageloader.loader.LoaderManager;

/**
 * 请求队列，所有图片加载的请求保存在该队列中
 *
 * @author zengjiantao
 * @since 2020-08-10
 */
public class RequestDispatcher extends Thread {
    private BlockingQueue<BitmapRequest> mRequestQueue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> requestQueue) {
        mRequestQueue = requestQueue;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                BitmapRequest request = mRequestQueue.take();
                Log.d("Debug", "处理请求：" + request.getSerialNo());
                //解析图片地址，获取对象的加载器
                String schema = parseSchema(request.getImageUri());
                Loader loader = LoaderManager.getInstance().getLoader(schema);
                loader.loadImage(request);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析图片地址，获取schema
     *
     * @param uri
     * @return
     */
    private String parseSchema(String uri) {
        if (uri.contains("://")) {
            return uri.split("://")[0];
        } else {
            Log.d("Debug", "图片地址Schema解析异常！");
        }
        return null;
    }
}
