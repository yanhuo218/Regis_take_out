package com.yanhuo.filter;


import com.alibaba.fastjson.JSON;
import com.yanhuo.common.BaseContext;
import com.yanhuo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURL = request.getRequestURI();
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(requestURL, urls);

        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        Long employee = (Long) request.getSession().getAttribute("employee");
        if (null != employee) {
            BaseContext.setId(employee);
            filterChain.doFilter(request, response);
            return;
        }

        Long UserId = (Long) request.getSession().getAttribute("user");
        if (null != UserId) {
            BaseContext.setId(UserId);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("拦截请求...:{}", request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public boolean check(String requestURL, String[] urls) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if (match) {
                return true;
            }
        }
        return false;
    }
}



