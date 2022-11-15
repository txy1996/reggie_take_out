package com.txy.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.txy.reggie.common.BaseContext;
import com.txy.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO 登录检查过滤器,检查用户是否完成登录
 * @date 2022/11/8 18:03
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        //1.获取本次请求的url
        String requestURI = request1.getRequestURI();
        log.info("本次请求的路径: {}",requestURI);
        String [] urls = new String[]{
               "/employee/login" ,
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg", //移动端发送短信
                "/user/login" //移动端登录
        };
        //2.判断本次请求是否需要处理
        boolean check = check(requestURI, urls);

        //3.如果不需要处理,则直接放行
        if(check){
            log.info("本次请求的路径: {} 不需要处理",requestURI);
            chain.doFilter(request1,response1);
            return;
        }
        //4-1.判断登录状态,如果已经登录,则直接放行
        if (request1.getSession().getAttribute("employee") != null){
            log.info("{} 用户已登录",request1.getSession().getAttribute("employee"));
            Long empId = (Long) request1.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            long id = Thread.currentThread().getId();
            log.info("线程id为: {}",id);

            chain.doFilter(request1,response1);
            return;
        }
        //4-1.判断登录状态,如果已经登录,则直接放行
        if (request1.getSession().getAttribute("user") != null){
            log.info("{} 用户已登录",request1.getSession().getAttribute("user"));
            Long userId = (Long) request1.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            long id = Thread.currentThread().getId();
            log.info("线程id为: {}",id);

            chain.doFilter(request1,response1);
            return;
        }
        //5.如果未登录则返回未登录结果,通过输出流的方式向客服端页面响应数据
        log.info("用户未登录");
        response1.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        // log.info("拦截到请求: {}",request1.getRequestURI());
        return;
    }

    /**
     *路径匹配,检查本次请求是否需要放行
     * @param requestURL
     * @return
     */
    public boolean check(String requestURL,String[] urls){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if(match){
                return true;
            }
        }
        return false;
    }
}
