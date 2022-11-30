package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.dao.ModuleMapper;
import com.zhang.crm.model.TreeModuleVo;
import com.zhang.crm.service.ModuleService;
import com.zhang.crm.vo.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 资源控制层
 */
@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ModuleMapper moduleMapper;

    /**
     * 打开角色授权页面
     *
     * @return
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPaged(Integer roleId, HttpServletRequest request) {
        request.setAttribute("roleId", roleId);
        return "role/grant";
    }

    /**
     * 查询所有的角色权限资源
     *
     * @return
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModuleVo> queryAllModules(Integer roleId) {
        return moduleService.queryAllModules(roleId);
    }


    /**
     * 查询所有的资源列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String, Object> queryModuleList() {
        return moduleService.queryModuleList();
    }

    /**
     * 进入资源页面视图
     *
     * @return
     */
    @RequiredPermission(code = "6030")
    @RequestMapping("index")
    public String index() {
        return "module/module";
    }

    /**
     * 进入添加资源的视图页面
     *
     * @param grade
     * @param parentId
     * @param request
     * @return
     */
    @RequestMapping("toAddModulePage")
    public String toAddModulePage(Integer grade, Integer parentId, HttpServletRequest request) {
        // 将数据设置到请求域中
        request.setAttribute("grade", grade);
        request.setAttribute("parentId", parentId);
        return "module/add";
    }


    /**
     * 添加资源操作
     *
     * @param module
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Module module) {
        moduleService.addModule(module);
        return success("添加资源操作成功");
    }

    /**
     * 进入修改模块视图页面
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("toUpdateModulePage")
    public String toUpdateModulePage(Integer id, HttpServletRequest request) {
        //将待修改的module设置到作用域中加载
        request.setAttribute("module", moduleMapper.selectByPrimaryKey(id));
        return "module/update";
    }

    /**
     * 修改模块操作
     *
     * @param module
     * @return
     */
        @RequestMapping("update")
        @ResponseBody
        public ResultInfo update(Module module) {
            moduleService.update(module);
            return success("修改模块操作成功");
        }

    /**
     * 删除模块操作
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public ResultInfo delete(Integer id) {
        moduleService.delete(id);
        return success("删除模块操作成功");
    }
}
