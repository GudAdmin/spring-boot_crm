package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.dao.CustomerMapper;
import com.zhang.crm.dao.CustomerOrderMapper;
import com.zhang.crm.query.CustomerQuery;
import com.zhang.crm.service.CustomerService;
import com.zhang.crm.vo.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户信息控制层
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 进入客户信息管理页面
     *
     * @return
     */
    @RequiredPermission(code = "2010")
    @RequestMapping("index")
    public String index() {
        return "customer/customer";
    }

    /**
     * 查找用户数据，加载用户数据页面
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(CustomerQuery customerQuery) {
        return customerService.queryCustomerQuery(customerQuery);
    }

    /**
     * 打开添加/修改顾客视图
     */
    @RequestMapping("toAddOrUpdateCustomerPage")
    public String toAddOrUpdateCustomerPage(Integer id, HttpServletRequest request) {
        if (id != null) {
            //修改操作，设置Customer到request域中
            request.setAttribute("customer", customerMapper.selectByPrimaryKey(id));
        }
        return "customer/add_update";
    }

    /**
     * 客户添加操作
     */
    @RequiredPermission(code = "201001")
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(Customer customer) {
        customerService.add(customer);
        return success("客户添加操作成功");
    }

    /**
     * 客户修改操作
     *
     * @param customer
     * @return
     */
    @RequiredPermission(code = "201002")
    @ResponseBody
    @RequestMapping("update")
    public ResultInfo update(Customer customer) {
        customerService.update(customer);
        return success("客户修改操作成功");
    }

    /**
     * 客户删除操作
     *
     * @param id
     * @return
     */
    @RequiredPermission(code = "201003")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id) {
        customerService.delete(id);
        return success("客户删除操作成功");
    }

    /**
     * 打开客户订单视图页面
     *
     * @param customerId
     * @param request
     * @return
     */
    @RequestMapping("toCustomerOrderPage")
    public String toCustomerOrderPage(Integer customerId, HttpServletRequest request) {
        if (customerId != null) {
            request.setAttribute("customer", customerMapper.selectByPrimaryKey(customerId));
        }
        return "customer/customer_order";
    }

    /**
     * 客户贡献分析
     *
     * @param customerQuery
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequiredPermission(code = "4010")
    @RequestMapping("queryCustomerContributionByParams")
    @ResponseBody
    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        return customerService.queryCustomerContributionByParams(customerQuery);
    }

    /**
     * 查询客户构成 （折线图）
     *
     * @param
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequestMapping("countCustomerMake")
    @ResponseBody
    public Map<String, Object> countCustomerMake() {
        return customerService.countCustomerMake();
    }

    /**
     * 查询客户构成 （饼状图）
     *
     * @param
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    @RequestMapping("countCustomerMake02")
    @ResponseBody
    public Map<String, Object> countCustomerMake02() {
        return customerService.countCustomerMake02();
    }
}
