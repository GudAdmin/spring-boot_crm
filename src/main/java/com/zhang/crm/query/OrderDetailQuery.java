package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单详情查询条件
 */
@Getter
@Setter
public class OrderDetailQuery extends BaseQuery {
    //订单Id
    private Integer orderId;
}
