package com.stateflow.system.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.stateflow.system.workflow.WorkflowTask;

public interface WorkflowTaskMapper
{
    @Select("select id, state, create_time as createTime, update_time as updateTime from wf_task where id = #{id}")
    WorkflowTask selectById(String id);

    @Insert("insert into wf_task(id, state, create_time, update_time) values(#{id}, #{state}, #{createTime}, #{updateTime})")
    int insert(WorkflowTask task);

    @Update("update wf_task set state = #{state}, update_time = #{updateTime} where id = #{id}")
    int updateState(@Param("id") String id, @Param("state") String state, @Param("updateTime") java.util.Date updateTime);
}
