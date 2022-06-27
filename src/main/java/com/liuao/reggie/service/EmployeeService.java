package com.liuao.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liuao.reggie.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService extends IService<Employee> {

    Page<Employee> selectMemberPage(int pageSize, int pageNum, String name);

    int modifyMemberStatus(long id, int status);
}
