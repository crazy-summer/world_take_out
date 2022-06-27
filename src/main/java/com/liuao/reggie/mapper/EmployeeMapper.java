package com.liuao.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuao.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    public int modifyMemberStatus(long id, int status);
}
