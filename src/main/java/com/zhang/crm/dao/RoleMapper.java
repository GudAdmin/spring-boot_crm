package com.zhang.crm.dao;
import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * 角色接口
 */
@Mapper
@Component
public interface RoleMapper extends BaseMapper<Role, Integer> {
    /**
     * 查询所有的角色列表
     * @param userId
     * @return
     */
    List<Map<String, Object>> queryAllRoles(Integer userId);

    /**
     *  通过角色名查询角色对象
     * @param roleName
     * @return
     */
    Role selectByRoleName(String roleName);

    /**
     * 删除角色操作
     * @param roleId
     * @return
     */
    Integer delete(Integer roleId);
}