package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.CustomerServe;
import org.apache.ibatis.annotations.Mapper;;
import org.springframework.stereotype.Component;

/**
 * 客户服务接口
 */
@Mapper
@Component
public interface CustomerServeMapper extends BaseMapper<CustomerServe, Integer> {

}