package com.scheduled.config;

import com.scheduled.service.Service;

import java.util.List;
import java.util.Map;

public class MyLampRunnable implements Runnable{
    private Service service;

    private String time;

    public MyLampRunnable(String time, Service service) {
        this.time = time;
        this.service = service;
    }

    /**
     * 开启线程, 有定时器就开始走此方法, 根据上面的time这个cron定时器表达式,查出需要控制的设备,进行控制...
     */
    @Override
    public void run() {
        System.err.println(time);
        System.err.println("定时器每个一秒执行一次");
    }
}
