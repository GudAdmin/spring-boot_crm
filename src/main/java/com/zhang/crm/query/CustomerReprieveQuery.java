package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户流失暂缓查询条件
 */
@Getter
@Setter
public class CustomerReprieveQuery extends BaseQuery {
    // 流失客户ID
    private Integer lossId;
}
