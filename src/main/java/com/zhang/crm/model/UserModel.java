package com.zhang.crm.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 定义 UserModel 实体类，用来返回登录成功后的用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel implements Serializable {
    private String userIdStr;
    private String userName;
    private String trueName;
}