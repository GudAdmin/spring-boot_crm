package com.zhang.crm.interceptors;

import com.zhang.crm.exceptions.NoLoginException;
import com.zhang.crm.service.UserService;
import com.zhang.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zb
 * @version 1.0
 * @date 2022/11/29 10:17
 * @ClassName NoLoginInterceptor
 * @Description TODD
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    /**
     * 判断用户是否是登录状态
     * 获取Cookie对象，解析用户ID的值
     * 如果用户ID不为空，且在数据库中存在对应的用户记录，表示请求合法
     * 否则，请求不合法，进行拦截，重定向到登录页面
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取Cookie的用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断用户ID是否不为空，且数据库中存在对应的用户记录
        if (null == userId || null == userService.selectByPrimaryKey(userId)) {
            // 抛出未登录异常
            throw new NoLoginException();
        }
        return true;
    }
}
