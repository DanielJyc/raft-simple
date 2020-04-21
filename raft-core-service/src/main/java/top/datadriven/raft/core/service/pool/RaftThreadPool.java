package top.datadriven.raft.core.service.pool;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @description:
 * @author: jiayancheng
 * @email: jiayancheng@foxmail.com
 * @datetime: 2020/4/19 5:50 下午
 * @version: 1.0.0
 */
public class RaftThreadPool {
    /**
     * 线程工厂，提供创建新线程的功能。
     */
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("raft-pool-%d").build();

    /**
     * 线程池
     */
    private static final ExecutorService executorService = new ThreadPoolExecutor(
            // 核心线程数，默认情况下核心线程会一直存活
            8,
            //线程池所能容纳的最大线程数。
            12,
            //非核心线程的闲置超时时间，超过这个时间就会被回收。
            10,
            TimeUnit.SECONDS,
            //线程池中的任务队列.（超过核心线程的数量，被放到这里）
            new LinkedBlockingDeque<>(1024),
            //线程工厂，提供创建新线程的功能。
            THREAD_FACTORY,
            //当达到最大线程数，且队列已满情况下，执行拒绝策略:抛异常
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 定义一个线程池，用于处理所有任务
     */
    final static ListeningExecutorService listeningExecutorService
            = MoreExecutors.listeningDecorator(executorService);

    /**
     * 执行任务
     */
    public static void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    /**
     * 执行任务
     */
    public static <T> ListenableFuture<T> execute(Callable<T> callable) {
        return listeningExecutorService.submit(callable);
    }
}
