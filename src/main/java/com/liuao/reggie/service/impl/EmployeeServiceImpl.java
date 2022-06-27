package com.liuao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuao.reggie.entity.Employee;
import com.liuao.reggie.mapper.EmployeeMapper;
import com.liuao.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
    public Page<Employee> selectMemberPage(int pageSize, int pageNum, String name) {
        // 构造分页器
        Page<Employee> pageInfo = new Page(pageNum, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        return page(pageInfo, queryWrapper);

    }

    @Override
    public int modifyMemberStatus(long id, int status) {
        return this.getBaseMapper().modifyMemberStatus(id, status);
    }
}
