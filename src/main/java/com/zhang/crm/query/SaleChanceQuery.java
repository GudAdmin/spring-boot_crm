package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 营销机会查询条件
 */
@Getter
@Setter

public class SaleChanceQuery extends BaseQuery {
    //父类两个分页参数

    //营销机会管理  条件查询
    //条件查询

    /**
     * 客户名
     */
    private String customerName;

    /**
     * 创建人
     */
    private String createMan;

    /**
     * 分配状态
     * 0:未分配
     * 1.已分配
     */
    private Integer state;


    //客户开发计划    条件查询
    private String devResult;//开发状态
    private Integer assignMan;//指派人
}
