package com.zhang.crm.controller;

import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.dao.CustomerServeMapper;
import com.zhang.crm.query.CustomerServeQuery;
import com.zhang.crm.service.CustomerServeService;
import com.zhang.crm.utils.LoginUserUtil;
import com.zhang.crm.vo.CustomerServe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户服务控制层
 */
@Controller
@RequestMapping("customer_serve")
public class CustomerServeController extends BaseController {
    @Autowired
    private CustomerServeService customerServeService;

    @Autowired
    private CustomerServeMapper customerServeMapper;

    /**
     * 通过不同的类型进入不同的服务页面
     *
     * @param type
     * @return java.lang.String
     */
    @RequestMapping("index/{type}")
    public String index(@PathVariable Integer type) {
        // 判断类型是否为空
        if (type != null) {
            if (type == 1) {

                // 服务创建
                return "customerServe/customer_serve";

            } else if (type == 2) {

                // 服务分配
                return "customerServe/customer_serve_assign";

            } else if (type == 3) {

                // 服务处理
                return "customerServe/customer_serve_proce";

            } else if (type == 4) {

                // 服务反馈
                return "customerServe/customer_serve_feed_back";

            } else if (type == 5) {

                // 服务归档
                return "customerServe/customer_serve_archive";

            } else {
                return "";
            }

        } else {
            return "";
        }
    }

    /**
     * 查询服务数据列表
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> queryCustomerServeByParams(CustomerServeQuery customerServeQuery,
                                                          Integer flag, HttpServletRequest request) {

        // 判断是否执行服务处理，如果是则查询分配给当前登录用户的服务记录
        if (flag != null && flag == 1) {
            // 设置查询条件：分配人
            customerServeQuery.setAssigner(LoginUserUtil.releaseUserIdFromCookie(request));
        }

        return customerServeService.queryCustomerServeByParams(customerServeQuery);
    }


    /**
     * 打开服务添加视图页面
     *
     * @return
     */
    @RequestMapping("toAddCustomerServePage")
    public String toAddCustomerServePage() {
        return "customerServe/customer_serve_add";
    }

    /**
     * 服务添加操作
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    public ResultInfo add(CustomerServe customerServe) {
        customerServeService.add(customerServe);
        return success("服务添加操作成功");
    }

    /**
     * 打开服务分配视图页面
     */
    @RequestMapping("toCustomerServeAssignPage")
    public String toCustomerServeAssignPage(Integer id, HttpServletRequest request) {
        if (id != null) {
            request.setAttribute("customerServe", customerServeMapper.selectByPrimaryKey(id));
        }
        return "customerServe/customer_serve_assign_add";
    }


    /**
     * 客户分配服务
     *
     * @param customerServe
     * @return
     */
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo update(CustomerServe customerServe) {
        customerServeService.update(customerServe);
        return success("客户服务更新成功");
    }

    /**
     * 打开服务处理视图
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping("toCustomerServeProcePage")
    public String toCustomerServeProcePage(Integer id, HttpServletRequest request) {
        if (id != null) {
            request.setAttribute("customerServe", customerServeMapper.selectByPrimaryKey(id));
        }
        return "customerServe/customer_serve_proce_add";
    }


    /**
     * 打开服务反馈页面
     *
     * @param id
     * @param model
     * @return java.lang.String
     */
    @RequestMapping("toCustomerServeFeedBackPage")
    public String toCustomerServeFeedBackPage(Integer id, Model model) {
        // 通过id查询服务记录，并设置到请求域中
        model.addAttribute("customerServe", customerServeService.selectByPrimaryKey(id));
        return "customerServe/customer_serve_feed_back_add";
    }

}
