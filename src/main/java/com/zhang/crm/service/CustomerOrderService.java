package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.CustomerOrderMapper;
import com.zhang.crm.query.CustomerOrderQuery;
import com.zhang.crm.vo.CustomerOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder, Integer> {
    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    public Map<String, Object> queryCustomerOrderByParams(CustomerOrderQuery customerOrderQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerOrderQuery.getPage(), customerOrderQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<CustomerOrder> pageInfo = new PageInfo<CustomerOrder>(customerOrderMapper.selectByParams(customerOrderQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 查询客户订单详情
     *
     * @param orderId
     * @return
     */
    public Map<String, Object> queryByOrderId(Integer orderId) {
        return customerOrderMapper.queryByOrderId(orderId);
    }
}
