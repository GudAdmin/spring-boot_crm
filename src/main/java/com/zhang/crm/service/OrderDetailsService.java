package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.OrderDetailsMapper;
import com.zhang.crm.query.OrderDetailQuery;
import com.zhang.crm.vo.OrderDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderDetailsService extends BaseService<OrderDetails, Integer> {
    @Autowired
    private OrderDetailsMapper orderDetailsMapper;

    /**
     * 加载订单详情数据列表
     *
     * @param orderDetailQuery
     * @return
     */
    public Map<String, Object> list(OrderDetailQuery orderDetailQuery) {
        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(orderDetailQuery.getPage(), orderDetailQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<OrderDetails> pageInfo = new PageInfo<OrderDetails>(orderDetailsMapper.selectByParams(orderDetailQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }
}
