package com.mingyuechunqiu.agile.util;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : xiyujieit@163.com
 *     time   : 2019/4/25
 *     desc   : 线程池工具类
 *     version: 1.0
 * </pre>
 */
public final class ThreadPoolUtils {

    private static volatile ExecutorService sExecutorService;

    private ThreadPoolUtils() {
    }

    /**
     * 执行一个任务
     *
     * @param runnable 任务执行体
     */
    public static void executeAction(@Nullable Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (sExecutorService != null && sExecutorService.isShutdown()) {
            sExecutorService = null;
        }
        checkOrCreateCacheThreadPool();
        sExecutorService.submit(runnable);
    }

    /**
     * 停止线程池执行
     *
     * @param isNow 是否立即停止
     */
    public static void shutDown(boolean isNow) {
        if (sExecutorService == null || sExecutorService.isShutdown()) {
            return;
        }
        if (isNow) {
            sExecutorService.shutdownNow();
        } else {
            sExecutorService.shutdown();
        }
        sExecutorService = null;
    }

    /**
     * 检查或创建缓存线程池
     */
    private static void checkOrCreateCacheThreadPool() {
        if (sExecutorService == null) {
            synchronized (ThreadPoolUtils.class) {
                if (sExecutorService == null) {
                    sExecutorService = Executors.newCachedThreadPool();
                }
            }
        }
    }
}
