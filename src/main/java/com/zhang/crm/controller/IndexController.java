package com.zhang.crm.controller;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.service.PermissionService;
import com.zhang.crm.service.UserService;
import com.zhang.crm.utils.LoginUserUtil;
import com.zhang.crm.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页控制层
 */
@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 系统登录页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 后台管理主页面
     *
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request) {
        //通过获取cookie用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user", user);

        //通过当前登录用户ID，查询当前登录用户拥有的资源列表(查询对应的资源授权码)
        List<String> permissions = null;
        permissions = permissionService.queryUserHasRoleHasPermissionByUserId(userId);
        //将集合设置作用域中（Session作用域)
        request.getSession().setAttribute("permissions", permissions);

        return "main";
    }
}