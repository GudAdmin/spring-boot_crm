package com.zhang.crm.controller;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.query.OrderDetailQuery;
import com.zhang.crm.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 订单详情控制层
 */
@Controller
@RequestMapping("order_details")
public class OrderDetailsController extends BaseController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    /**
     * 加载订单详情数据列表
     *
     * @param orderDetailQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(OrderDetailQuery orderDetailQuery) {
        return orderDetailsService.list(orderDetailQuery);
    }
}
