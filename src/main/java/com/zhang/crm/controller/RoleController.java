package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.dao.RoleMapper;
import com.zhang.crm.query.RoleQuery;
import com.zhang.crm.service.RoleService;
import com.zhang.crm.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 角色控制层
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 查询所有角色，加载添加用户时指定角色的下拉框
     *
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleService.queryAllRoles(userId);
    }


    /**
     * 进入角色管理视图
     *
     * @return
     */
    @RequiredPermission(code = "6020")
    @RequestMapping("index")
    public String index() {
        return "role/role";
    }

    /**
     * 查询所有的角色
     *
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryAllRoles(RoleQuery roleQuery) {
        return roleService.selectByParamsForTables(roleQuery);
    }

    /**
     * 进入角色修改/添加页面
     *
     * @param roleId
     * @param request
     * @return
     */
    @RequestMapping("toAddOrUpdateRolePage")
    public String toAddOrUpdateRolePage(Integer roleId, HttpServletRequest request) {
        //判断如果id不为空，则为修改操作，将id对应的角色放入数据域
        if (roleId != null) {
            Role role = roleMapper.selectByPrimaryKey(roleId);
            request.setAttribute("role", role);
        }
        return "role/add_update";
    }

    /**
     * 角色添加操作
     *
     * @param role
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Role role) {
        roleService.add(role);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("添加角色操作成功");
        return resultInfo;
    }

    /**
     * 角色修改操作
     *
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo update(Role role) {
        roleService.update(role);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("修改角色操作成功");
        return resultInfo;
    }

    /**
     * 删除角色操作
     *
     * @param roleId
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer roleId) {
        roleService.delete(roleId);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setMsg("删除角色操作成功");
        return resultInfo;
    }

    /**
     * 角色授权操作
     *
     * @param roleId
     * @param mIds
     * @return
     */
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId, Integer[] mIds) {
        roleService.addGrant(roleId, mIds);
        return success("角色授权操作成功");
    }
}
