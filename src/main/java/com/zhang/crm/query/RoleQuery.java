package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Data;

/**
 * 角色查询条件
 */
@Data
public class RoleQuery extends BaseQuery {
    private String roleName;
}
