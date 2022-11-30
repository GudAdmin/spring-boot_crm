package com.zhang.crm.controller;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.dao.CustomerOrderMapper;
import com.zhang.crm.query.CustomerOrderQuery;
import com.zhang.crm.service.CustomerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 客户订单控制层
 */
@Controller
@RequestMapping("order")
public class CustomerOrderController extends BaseController {
    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 查询客户订单列表
     *
     * @param customerOrderQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(CustomerOrderQuery customerOrderQuery) {
        return customerOrderService.queryCustomerOrderByParams(customerOrderQuery);
    }

    /**
     * 进入查看订单详情视图页面
     *
     * @param orderId
     * @param request
     * @return
     */
    @RequestMapping("toOrderDetailPage")
    public String toOrderDetailPage(Integer orderId, HttpServletRequest request) {

        if (orderId != null) {
            Map<String, Object> map = customerOrderService.queryByOrderId(orderId);
            request.setAttribute("order", map);
        }
        return "customer/customer_order_detail";
    }
}
