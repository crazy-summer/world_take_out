package com.liuao.reggie.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liuao.reggie.common.R;
import com.liuao.reggie.entity.Employee;
import com.liuao.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request){
        // 对用户密码进行md5加密
        String jiamiPwd = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        // 根据用户名查询是否有该用户，没有返回fasle
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if(emp==null){
            return R.error("用户不存在!");
        }
        // 验证密码是否正确
        if(!jiamiPwd.equals(emp.getPassword())){
            return R.error("密码不正确!");
        }
        // 查看员工状态是否禁用，是的话返回员工已被禁用
        if(emp.getStatus() != 1){
            return R.error("员工已被禁用!");
        }
        // 将用户id存入session
        request.getSession().setAttribute("empId", emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logOut(HttpServletRequest request){
        System.out.println(request);
        request.getSession().removeAttribute("empId");
        return R.success("登出成功!");
    }

    /**
     * 新增员工
     * @param employee
     * @param request
     * @return
     */
    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee, HttpServletRequest request){
        log.info("新增员工参数为:{}",employee);
        log.info("当前前程id是:"+Thread.currentThread().getId());
        String pwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(pwd);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        long empId = (long) request.getSession().getAttribute("empId");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增成功!");
    }

    @GetMapping("/page")
    public R<Page<Employee>> selectMemberPage( int pageSize, @RequestParam("page")int pageNum, String name){
        Page<Employee> page = employeeService.selectMemberPage(pageSize, pageNum, name);
        return R.success(page);
    }

    @PutMapping
    public R<String> modifyMemberStatus(@RequestBody Employee employee, HttpServletRequest request){

        // 如果是启用禁用按钮发过来的修改员工状态的请求，传入对象参数只有id和status字段，先根据id去数据库查询出完整的对象
//        Employee e = null;
//        if(employee.getUsername()==null){
//            e = employeeService.getById(employee.getId());
//            e.setStatus(employee.getStatus());
//        }
//        else {
//            e = employee;
//        }
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("empId"));
        log.info("修改员工参数为:{}",employee);
        log.info("当前前程id是:"+Thread.currentThread().getId());
        boolean flag = employeeService.updateById(employee);
        if(flag){
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    @GetMapping("/{id}")
    public R<Employee> getEditMemberInfo(@PathVariable("id") String id){
        Employee e = employeeService.getById(id);
        return R.success(e);
    }
}
