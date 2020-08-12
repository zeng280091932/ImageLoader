package com.beauney.imageloader.request;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zengjiantao
 * @since 2020-08-10
 */
public class RequestQueue {
    //队列
    //多线程的数据共享
    //阻塞队列
    //生成效率和消费效率相差甚远，处理好兼顾效率和线程安全问题
    //concurrent出现了
    //优先级的阻塞队列
    //1.当队列中没有产品时，消费者被阻塞
    //2.使用优先级，优先级高的产品会被优先消费
    //前提：每个产品的都有一个编号（实例化出来的先后顺序）
    //优先级的确定，受产品编号的影响，但是由加载策略所决定
    private BlockingQueue<BitmapRequest> mRequestQueue = new PriorityBlockingQueue<>();

    //转发器的数量
    private int mThreadCount;
    //一组转发器
    private RequestDispatcher[] mDispatchers;
    //i++ ++i线程不安全
    //线程安全
    private AtomicInteger mAi = new AtomicInteger(0);

    public RequestQueue(int threadCount) {
        mThreadCount = threadCount;
    }

    public void addRequest(BitmapRequest request) {
        if (!mRequestQueue.contains(request)) {
            //给请求编号
            request.setSerialNo(mAi.incrementAndGet());
            mRequestQueue.add(request);
            Log.d("Debug", "添加请求" + request.getSerialNo());
        } else {
            Log.d("Debug", "请求已经存在" + request.getSerialNo());
        }
    }

    /**
     * 开始
     */
    public void start() {
        //先停止，再启动
        stop();
        startDispatchers();
    }

    /**
     * 停止
     */
    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                //打断
                mDispatchers[i].interrupt();
            }
        }
    }

    private void startDispatchers() {
        mDispatchers = new RequestDispatcher[mThreadCount];
        //初始化所有的转发器
        for (int i = 0; i < mThreadCount; i++) {
            RequestDispatcher dispatcher = new RequestDispatcher(mRequestQueue);
            mDispatchers[i] = dispatcher;
            //启动线程
            mDispatchers[i].start();
        }
    }
}
