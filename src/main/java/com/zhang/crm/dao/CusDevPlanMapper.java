package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.CusDevPlan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 客户开发计划接口
 */
@Mapper
@Component
public interface CusDevPlanMapper extends BaseMapper<CusDevPlan, Integer> {


}