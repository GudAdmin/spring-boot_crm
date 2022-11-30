package com.zhang.crm.service;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.PermissionMapper;
import com.zhang.crm.vo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends BaseService<Permission, Integer> {
    @Autowired
    private PermissionMapper permissionMapper;


    /**
     * 通过用户id查询用户拥有的角色的所拥有的所有资源的权限码
     *
     * @param userId
     * @return
     */
    public List<String> queryUserHasRoleHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasRoleHasPermissionByUserId(userId);
    }
}
