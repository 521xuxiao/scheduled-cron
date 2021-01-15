package com.scheduled;

import com.scheduled.config.MyLampTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ScheduledApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ConfigurableApplicationContext = SpringApplication.run(ScheduledApplication.class, args);

        ConfigurableApplicationContext.getBean(MyLampTask.class).startLampTask(); // 动态定时器起项目就加载
    }
}
