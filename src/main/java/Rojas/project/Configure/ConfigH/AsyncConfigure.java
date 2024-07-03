package Rojas.project.Configure.ConfigH;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfigure implements AsyncConfigurer {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Número de hilos básicos
        executor.setMaxPoolSize(50);  // Número máximo de hilos
        executor.setQueueCapacity(100); // Capacidad de la cola
        executor.setThreadNamePrefix("Async-"); // Prefijo para los nombres de los hilos
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }
}
