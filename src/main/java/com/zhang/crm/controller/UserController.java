package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.model.UserModel;
import com.zhang.crm.query.UserQuery;
import com.zhang.crm.service.UserService;
import com.zhang.crm.utils.LoginUserUtil;
import com.zhang.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户控制层
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 登录功能
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        //通过try catch 捕获service层的异常，如果Service出现异常，登录失败，否侧登录成功
        //调用service层方法
        UserModel userVo = userService.userLogin(userName, userPwd);
        //设置resultInfo的result值，将数据返回给请求
        resultInfo.setResult(userVo);
        return resultInfo;
    }

    /**
     * 用户修改密码
     *
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,
                                         String oldPassword, String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();
        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Service中的修改密码方法
        userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);

        return resultInfo;
    }

    /**
     * 进入修改密码的页面
     *
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }


    //查询所有的销售人员
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }


    /**
     * 进入用户管理页面
     *
     * @return
     */
    @RequiredPermission(code = "6010")
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }


    /**
     * 查询所有的用户
     *
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        return userService.queryUserByParams(userQuery);
    }

    /**
     * 进入添加或更新用户页面
     *
     * @return
     */
    @RequestMapping("toAddOrUpdateUserPage")
    public String addUserPage(Integer id, HttpServletRequest request) {
        if (id != null) {
            //若为更新，将待更新的数据添加到数据域
            request.setAttribute("userInfo", userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }

    /**
     * 添加用户操作
     *
     * @param user
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("用户添加操作成功");
        return resultInfo;
    }

    /**
     * 修改用户操作
     *
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("修改用户操作成功");
        return resultInfo;
    }

    /**
     * 删除多条用户操作
     *
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteUserByIds(ids);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("删除用户操作成功");
        return resultInfo;
    }

    /**
     * 查询所有的客户经理
     *
     * @return
     */
    @RequestMapping("queryAllCustomerManagers")
    @ResponseBody
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userService.queryAllCustomerManagers();
    }

}
