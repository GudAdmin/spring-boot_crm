package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.*;

/**
 * 用户管理多条件查询
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery extends BaseQuery {
    private String userName;//用户名称
    private String email;//邮箱
    private String phone;//手机号码
}
