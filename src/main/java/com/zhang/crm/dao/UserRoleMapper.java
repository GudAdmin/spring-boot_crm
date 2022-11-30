package com.zhang.crm.dao;
import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 用户角色接口
 */
@Mapper
@Component
public interface UserRoleMapper extends BaseMapper<UserRole, Integer> {
    /**
     * 查询用户的角色记录
     * @param userId
     * @return
     */
    Integer queryUserRolesCountById(Integer userId);

    /**
     * !--通过用户Id，删除对应的角色记录
     * @param userId
     * @return
     */
    int deleteAllRolesByUserId(Integer userId);
}