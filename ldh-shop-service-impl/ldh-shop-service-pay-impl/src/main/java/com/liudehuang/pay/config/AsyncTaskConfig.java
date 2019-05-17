package com.liudehuang.pay.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @author liudehuang
 * @date 2019/5/17 13:38
 */
@Configuration
@Slf4j
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {
    /**
     * 最小线程数(核心线程数)
     */
    @Value("${threadPool.corePoolSize}")
    private int corePoolSize;

    /**
     * 最大线程数
     */
    @Value("${threadPool.maxPoolSize}")
    private int maxPoolSize;
    /**
     * 等待队列(队列最大长度)
     */
    @Value("${threadPool.queueCapacity}")
    private int queueCapacity;

    @Bean(name="taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //最小线程数
        taskExecutor.setCorePoolSize(this.corePoolSize);
        //最大线程数
        taskExecutor.setMaxPoolSize(this.maxPoolSize);
        //等待队列(队列最大等待长度)
        taskExecutor.setQueueCapacity(this.queueCapacity);
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * 异步异常处理
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return new SpringAsyncExceptionHandler();
    }


    class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            log.error("Exception occurs in async method", throwable.getMessage());
        }

    }
}
