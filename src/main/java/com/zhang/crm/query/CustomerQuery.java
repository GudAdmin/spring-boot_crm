package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户信息查询条件
 */
@Getter
@Setter
public class CustomerQuery extends BaseQuery {
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户编号
     */
    private String customerNo;
    /**
     * 客户级别
     */
    private String level;
    /**
     * 订单金额
     * 金额区间  1=1-1000 2=1000-3000  3=3000-5000  4=5000以上
     */
    private String type;
    /**
     * 订单时间
     */
    private String time;
}
