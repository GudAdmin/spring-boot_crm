package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户开发计划查询条件
 */
@Getter
@Setter
public class CusDevPlanQuery extends BaseQuery {
    //营销机会ID
    private Integer saleChanceId;
}
