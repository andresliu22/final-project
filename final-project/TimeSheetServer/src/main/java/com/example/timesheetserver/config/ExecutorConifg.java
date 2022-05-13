package com.example.timesheetserver.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class ExecutorConifg {
    @Bean
    public Executor customExecutor() {
        /*
        corePoolSize is the minimum number of threads used by the pool. 2
        The number can increase up to maxPoolSize . When the load goes down, the pool will shrink back to corePoolSize .
         */

        /*
        5
        10 -> 2 into thread -> 8 should put into queue(5) -> 3
        Queue Capacity : Queue is using when all core pool are filled. Threads will be scalable to maximum pool size when queue is full.
         */

        /*
        3 -> 2
        Maximum Pool Size : Maximum pool size defines maximum parallel threads can run at same time. In our sample MAX_POOL_SIZE = 100 and this mean,
        our application can be increase to 100 parallel running threads when queue is full.
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("CustomExecutor-");
        executor.initialize();
        return executor;
    }


}
