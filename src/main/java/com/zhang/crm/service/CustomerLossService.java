package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.CustomerLossMapper;
import com.zhang.crm.query.CustomerLossQuery;
import com.zhang.crm.vo.CustomerLoss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss, Integer> {
    @Autowired
    private CustomerLossMapper customerLossMapper;

    public Map<String, Object> list(CustomerLossQuery customerLossQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerLossQuery.getPage(), customerLossQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<CustomerLoss> pageInfo = new PageInfo<>(customerLossMapper.selectByParams(customerLossQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }
}
