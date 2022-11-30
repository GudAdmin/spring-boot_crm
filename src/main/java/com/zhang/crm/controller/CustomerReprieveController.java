package com.zhang.crm.controller;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.base.ResultInfo;
import com.zhang.crm.dao.CustomerReprieveMapper;
import com.zhang.crm.query.CustomerReprieveQuery;
import com.zhang.crm.service.CustomerReprieveService;
import com.zhang.crm.vo.CustomerReprieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户服务控制层
 */
@Controller
@RequestMapping("customer_rep")
public class CustomerReprieveController extends BaseController {
    @Autowired
    private CustomerReprieveService customerReprieveService;

    @Autowired
    private CustomerReprieveMapper customerReprieveMapper;

    /**
     * 查询客户流失暂缓详情
     *
     * @param customerReprieveQuery
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public Map<String, Object> list(CustomerReprieveQuery customerReprieveQuery) {
        return customerReprieveService.list(customerReprieveQuery);
    }

    /**
     * 打开添加/修改客户流失暂缓视图页面
     *
     * @param lossId
     * @param request
     * @return
     */
    @RequestMapping("toAddOrUpdateCustomerReprPage")
    public String toAddOrUpdateCustomerReprPage(Integer lossId, HttpServletRequest request, Integer id) {
        //客户流失Id存到作用域中
        request.setAttribute("lossId", lossId);
        if (id != null) {
            CustomerReprieve customerReprieve = customerReprieveMapper.selectByPrimaryKey(id);
            request.setAttribute("customerRep", customerReprieve);
        }

        return "customerLoss/customer_rep_add_update";
    }

    /**
     * 添加暂缓措施
     *
     * @param customerReprieve
     * @return
     */
    @ResponseBody
    @RequestMapping("add")
    public ResultInfo add(CustomerReprieve customerReprieve) {
        customerReprieveService.add(customerReprieve);
        return success("添加暂缓措施成功");
    }

    /**
     * 修改暂缓措施操作
     *
     * @param customerReprieve
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(CustomerReprieve customerReprieve) {
        customerReprieveService.update(customerReprieve);
        return success("修改暂缓措施成功");
    }

    /**
     * 删除暂缓措施操作
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public ResultInfo delete(Integer id) {
        customerReprieveService.delete(id);
        return success("删除暂缓措施操作成功");
    }
}
