package com.txy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.txy.reggie.common.Result;
import com.txy.reggie.entity.Employee;
import com.txy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 员工
 * @date 2022/11/7 21:29
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1.将页面提交的密码惊喜MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败结果
        if(employee1 == null) {
            return Result.error("登录失败,用户不存在!");
        } else if(!employee1.getPassword().equals(password)) { //4.密码比对,如果不一致则返回密码错误
            return Result.error("登录失败,密码错误,请重新输入正确的密码!");
        } else if(employee1.getStatus() == 0) {
            return Result.error("登录失败,账号已禁用!"); //5.查看员工状态,如果为已禁用则返回员工已禁用
        }
        //6.登录成功将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee",employee1.getId());
        return Result.success(employee1);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        // 清楚session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功!");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize, String name){


        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);
        //构造条件构造器

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加查询条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> save(HttpServletRequest request,@RequestBody Employee employee) {
        log.info("新增员工,员工信息 {}",employee);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername())
                .or().eq(Employee::getPhone,employee.getPhone())
                .or().eq(Employee::getIdNumber,employee.getIdNumber());
        List<Employee> list = employeeService.list(queryWrapper);
        for (Employee employee1 : list) {
            if(employee1.getName().equals(employee.getName())) {
                return Result.error("用户名已存在!");
            } else if(employee1.getPhone().equals(employee.getPhone())) {
                return Result.error("手机号码已存在!");
            }else if(employee1.getIdNumber().equals(employee.getIdNumber())){
                return Result.error("身份证号码已存在!");
            }
        }

        //设置初始密码为123456 ,并使用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        // //获得当前登录用户id
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setCreateUser(empId);
        // employee.setUpdateUser(empId);
        employeeService.save(employee);
        return Result.success("新增员工成功!");
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request,@RequestBody Employee employee) {
        log.info("禁用启用员工 {}",employee);
        //获得当前登录用户id
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empId);
        // employee.setUpdateTime(LocalDateTime.now());
        long id = Thread.currentThread().getId();
        log.info("线程id为: {}",id);
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");

    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> queryEmployeeById (@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return Result.success(employee);
        }
        return Result.error("没有查询到对应员工信息");
    }

}
