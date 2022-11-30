package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.CustomerOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 客户订单接口
 */
@Mapper
@Component
public interface CustomerOrderMapper extends BaseMapper<CustomerOrder, Integer> {
    /**
     * 通过订单ID查询对应的订单记录
     * @param orderId
     * @return
     */
    Map<String, Object> queryByOrderId(Integer orderId);

    /**
     * 查询指定客户的最后一条订单记录
     * @param id
     * @return
     */
    CustomerOrder queryCustomerLastOrderByCustomerId(Integer id);
}