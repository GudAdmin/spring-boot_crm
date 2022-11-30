package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.OrderDetails;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 订单详情接口
 */
@Mapper
@Component
public interface OrderDetailsMapper extends BaseMapper<OrderDetails, Integer> {

}