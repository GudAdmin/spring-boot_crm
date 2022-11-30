package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.CustomerReprieve;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 客户流失暂缓接口
 */
@Mapper
@Component
public interface CustomerReprieveMapper extends BaseMapper<CustomerReprieve, Integer> {


}