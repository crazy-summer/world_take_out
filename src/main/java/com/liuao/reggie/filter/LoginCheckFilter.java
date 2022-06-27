package com.liuao.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.liuao.reggie.common.BaseContext;
import com.liuao.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求页面时，判断是否有登陆过
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    // 检查路径是否匹配
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("拦截到请求：{}", request.getRequestURL());
        log.info("当前线程id是:"+Thread.currentThread().getId());

        // 1.检查是否需要拦截
        String[] noNeedCheckPaths = new String[]{
                "/backend/**",
                "/backend/**",
                "/employee/login",
                "/employee/logout",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        if(isMatcher(noNeedCheckPaths, request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }
        // 2.检查是否有登陆过
        if(request.getSession().getAttribute("empId")!=null){
            BaseContext.setThreadLocal((Long) request.getSession().getAttribute("empId"));
            filterChain.doFilter(request, response);
            return;
        }
        if(request.getSession().getAttribute("userId")!=null){
            BaseContext.setThreadLocal((Long) request.getSession().getAttribute("userId"));
            filterChain.doFilter(request, response);
            return;
        }
        // 3.没有登陆过，返回信息，不放行
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    public static boolean isMatcher(String[] paths, String uri){
        for (String path : paths) {
            if(PATH_MATCHER.match(path, uri)){
                return true;
            }
        }
        return false;
    }
}
