package com.scheduled.controller;

import com.scheduled.returnData.ReturnData;
import com.scheduled.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Controller
@ResponseBody
public class Controller {
    @Autowired(required = false)
    private Service service;

    /**
     * 增加定时器
     * @param params
     * @return
     */
    @PostMapping("/addTaskTime")
    public ReturnData addTaskTime(@RequestBody Map<String, Object> params) {
        service.addTaskTime(params);
        return new ReturnData("新增成功");
    }

    /**
     * 删除定时器
     */
    @PostMapping("/deleteTaskTime")
    public ReturnData deleteTaskTime(@RequestBody Map<String, Object> params){
        service.deleteTaskTime(params);
        return new ReturnData("删除成功");
    }
}
