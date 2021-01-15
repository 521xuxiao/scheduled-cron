package com.scheduled.config;

import com.scheduled.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@EnableScheduling
public class MyLampTask implements SchedulingConfigurer {

    @Autowired
    private Service service;

    private final String FIELD_SCHEDULED_FUTURES = "scheduledFutures";
    private Set<ScheduledFuture<?>> scheduledFutures = new HashSet<>();
    private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<String, ScheduledFuture<?>>();
    private ScheduledTaskRegistrar taskRegistrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        this.taskRegistrar = scheduledTaskRegistrar;
    }


    /**
     * 启动类里面加配置  applicationContext.getBean(MyLampTask.class).startLampTask();   项目起来就查数据库里面的定时器放在次配置类里面
     * 的 scheduledFutures set集合中
     * @param cron
     * @return
     */
    public void startLampTask() {
        List<Map<String,Object>> timelist = service.queryLampTime();
        for(int i=0;i<timelist.size();i++) {
            String cron = timelist.get(i).get("cron").toString();
            if(!taskFutures.containsKey(cron)) {
                //taskRegistrar.setScheduler(null);
                TaskScheduler scheduler = taskRegistrar.getScheduler();
                ScheduledFuture<?> future = scheduler.schedule(new MyLampRunnable(cron,service), getTrigger(cron));
                scheduledFutures.add(future);
                taskFutures.put(cron, future);
            }
        }
    }

    /**
     * 动态的添加定时器 业务service层里面调用此方法
     * @param map
     */
    public void addLamptimetask(Map<String,Object> map) {
        String cron = map.get("cron").toString();
        if(!taskFutures.containsKey(cron)) {
            // int i = commonService.addLampTime(map);
            TaskScheduler scheduler = taskRegistrar.getScheduler();
            ScheduledFuture<?> future = scheduler.schedule(new MyLampRunnable(cron,service), getTrigger(cron));
            getScheduledFutures().add(future);
            taskFutures.put(cron, future);
        }
    }

    /**
     * 全部删除定时器
     */
    public void clearLamptimetask() {
        Iterator<ScheduledFuture<?>> it = scheduledFutures.iterator();
        while (it.hasNext()) {
            ScheduledFuture<?> future = it.next();
            future.cancel(true);
            it.remove();
            //String str = it.next();
            //.out.println(str);
        }
    }

    /**
     * 根据传入的参数删除定时器
     * @param list
     */
    public void clearLamptimetask(List<Map<String,Object>> list) {
        for(int i=0;i<list.size();i++) {
            String cron = list.get(i).get("cron").toString();
            ScheduledFuture<?> future = taskFutures.get(cron);
            if (future != null) {
                future.cancel(true);
            }
            taskFutures.remove(cron);
            getScheduledFutures().remove(future);
        }
    }



    private Trigger getTrigger(String cron) {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                // 触发器
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }
    private Set<ScheduledFuture<?>> getScheduledFutures() {
        if (scheduledFutures == null) {
            try {
                scheduledFutures = (Set<ScheduledFuture<?>>) getProperty(taskRegistrar, FIELD_SCHEDULED_FUTURES);
            }catch (NoSuchFieldException e){
                //throw new SchedulingException("not found scheduledFutures field.");
            }
        }
        return scheduledFutures;
    }
    public static Object getProperty(Object obj, String name) throws NoSuchFieldException {
        Object value = null;
        Field field = findField(obj.getClass(), name);
        if (field == null) {
            throw new NoSuchFieldException("no such field [" + name + "]");
        }
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            value = field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(accessible);
        return value;
    }

    public static Field findField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException ex)  {
            return findDeclaredField(clazz, name);
        }
    }

    public static Field findDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredField(clazz.getSuperclass(), name);
            }
            return null;
        }
    }
}
