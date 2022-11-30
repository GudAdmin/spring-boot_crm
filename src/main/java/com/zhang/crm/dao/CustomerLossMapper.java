package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.CustomerLoss;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 客户流失接口
 */
@Mapper
@Component
public interface CustomerLossMapper extends BaseMapper<CustomerLoss, Integer> {

}