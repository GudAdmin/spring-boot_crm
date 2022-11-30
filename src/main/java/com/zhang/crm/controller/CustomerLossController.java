package com.zhang.crm.controller;
import com.zhang.crm.annotation.RequiredPermission;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.dao.CustomerLossMapper;
import com.zhang.crm.query.CustomerLossQuery;
import com.zhang.crm.service.CustomerLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户流失控制层
 */
@Controller
@RequestMapping("customer_loss")
public class CustomerLossController extends BaseController {
    @Autowired
    private CustomerLossService customerLossService;

    @Autowired
    private CustomerLossMapper customerLossMapper;

    /**
     * 打开客户流失管理页面
     *
     * @return
     */
    @RequiredPermission(code = "2020")
    @RequestMapping("index")
    public String index() {
        return "customerLoss/customer_loss";
    }

    /**
     * 客户流失数据查询
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(CustomerLossQuery customerLossQuery) {
        return customerLossService.list(customerLossQuery);
    }

    /**
     * 打开客户流失添加/暂缓页面
     *
     * @param lossId
     * @return
     */
    @RequiredPermission(code = "202001")
    @RequestMapping("toCustomerLossPage")
    public String toCustomerLossPage(Integer lossId, HttpServletRequest request) {
        if (lossId != null) {
            request.setAttribute("customerLoss", customerLossMapper.selectByPrimaryKey(lossId));
        }
        return "customerLoss/customer_rep";
    }
}
