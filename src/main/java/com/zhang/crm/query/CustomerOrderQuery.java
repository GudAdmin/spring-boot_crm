package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户订单查询条件
 */
@Getter
@Setter
public class CustomerOrderQuery extends BaseQuery {
    //客户ID
    private Integer cusId;
}
