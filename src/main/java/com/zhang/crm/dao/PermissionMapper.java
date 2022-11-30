package com.zhang.crm.dao;
import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 权限接口
 */
@Mapper
@Component
public interface PermissionMapper extends BaseMapper<Permission, Integer> {
    /**
     * 根据id查询权限数量
     * @param roleId
     * @return
     */
    Integer selectRoleCountByPrimaryKey(Integer roleId);

    /**
     * 根据id删除权限
     * @param roleId
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 通过角色ID查询拥有的资源list
     * @param roleId
     * @return
     */
    List<Integer> queryRoleModuleIdsByRoleId(Integer roleId);

    /**
     * 通过用户id查询对应的资源的资源权限码列表
     * @param userId
     * @return
     */
    List<String> queryUserHasRoleHasPermissionByUserId(Integer userId);

    /**
     * 通过资源id查询权限
     * @param id
     * @return
     */
    Integer selectByModuleId(Integer id);

    /**
     * 通过资源id删除权限
     * @param id
     * @return
     */
    Integer deleteByModuleId(Integer id);
}