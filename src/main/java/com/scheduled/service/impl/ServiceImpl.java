package com.scheduled.service.impl;

import com.scheduled.config.MyLampTask;
import com.scheduled.dao.Dao;
import com.scheduled.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service {
    @Autowired(required = false)
    private Dao dao;
    @Autowired
    private MyLampTask myLampTask;

    /**
     * 查询数据库里面的定时器 cron 放到定时器的配置类set集合中  (项目启动的时候查出来数据就开始定时任务)
     * @return
     */
    @Override
    public List<Map<String, Object>> queryLampTime() {
        return dao.queryLampTime();
    }

    ///////////////////////////////////////////////////////////////////////

    /**
     * 下达定时任务
     * @param params   {orderName, orderId}
     */
    @Override
    public void addTaskTime(Map<String, Object> params) {
        String cron = "*/10 * * * * ?";
        params.put("cron", cron);
        dao.addTaskTime(params);   // 往数据库里面放
        myLampTask.addLamptimetask(params);  // 往MyLampTask定时器配置类里面放
    }

    /**
     * 删除定时器
     * @param params {id: '1,2'}
     */
    @Transactional
    @Override
    public void deleteTaskTime(Map<String, Object> params) {
        String id = params.get("id").toString();
        List<String> list = new ArrayList<>();
        if(id.indexOf(",") != -1) {
            String[] idArray = id.split(",");
            for (String itemId: idArray) {
                list.add(itemId);
            }
        }else{
            list.add(id);
        }
        List<Map<String, Object>> list2 = dao.queryTaskTime(list);
        dao.deleteTaskTime(list);  // 删除数据库里面的定时数据
        myLampTask.clearLamptimetask(list2);
    }
}
