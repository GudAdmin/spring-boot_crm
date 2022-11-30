package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户流失查询条件
 */
@Getter
@Setter
public class CustomerLossQuery extends BaseQuery {
    private String customerNo; // 客户编号
    private String customerName; // 客户名称
    private Integer state; // 流失状态  0=暂缓流失状态  1=确认流失状态
}
