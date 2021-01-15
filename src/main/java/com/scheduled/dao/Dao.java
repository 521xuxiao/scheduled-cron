package com.scheduled.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface Dao {

    List<Map<String, Object>> queryLampTime();

    ////////////////////////////////////////////////////////////////////

    void addTaskTime(Map<String, Object> params);

    void deleteTaskTime(@Param("list") List<String> list);

    List<Map<String, Object>> queryTaskTime(@Param("list1") List<String> list);
}
