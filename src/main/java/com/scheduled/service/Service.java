package com.scheduled.service;

import java.util.List;
import java.util.Map;

public interface Service {

    List<Map<String, Object>> queryLampTime();

    ///////////////////////////////   /////////////////////////////////////


    void addTaskTime(Map<String, Object> params);

    void deleteTaskTime(Map<String, Object> params);
}
