package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.ModuleMapper;
import com.zhang.crm.dao.PermissionMapper;
import com.zhang.crm.dao.RoleMapper;
import com.zhang.crm.query.RoleQuery;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.vo.Permission;
import com.zhang.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;


    /**
     * 查询角色列表
     *
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 查询所有的角色信息
     *
     * @param roleQuery
     * @return
     */
    public Map<String, Object> selectByParamsForTables(RoleQuery roleQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getLimit());
        //得到分页对象(对SaleChance对象进行分页）
        PageInfo<Role> pageInfo = new PageInfo<>(roleMapper.selectByParams(roleQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加用户角色操作
     * 1.进行roleName判断（存在且唯一)
     * 2.设置默认参数
     * 1.createDate
     * 2.updateDate
     * 3.isValid
     * 3.判断受影响的行数
     *
     * @param role
     */
    @Transactional
    public void add(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名称不能为空");
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null, "该角色已经存在");
        //设置默认值
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(roleMapper.insertSelective(role) != 1, "添加角色操作失败");
    }

    /**
     * 修改角色操作
     * 1.进行roleName判断，存在且唯一
     * 2.设置默认参数
     * updateTime的时间
     * 3.判断受到影响的行数
     *
     * @param role
     */
    public void update(Role role) {
        //判断角色名存在
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "角色名不能为空");
        //判断角色名唯一
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        if (temp != null) {
            AssertUtil.isTrue(!temp.getId().equals(role.getId()), "该角色已经存在");
        }
        //设置默认值参数
        role.setUpdateDate(new Date());
        //判断受影响的行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) != 1, "修改角色操作失败");
    }

    /**
     * 删除角色操作
     * 1.判断参数，roleId存在且对应的记录存在
     * 2.执行删除操作，判断受到影响的行数
     *
     * @param roleId
     */
    @Transactional
    public void delete(Integer roleId) {
        AssertUtil.isTrue(roleId == null, "待删除的角色记录不存在");
        AssertUtil.isTrue(roleMapper.selectByPrimaryKey(roleId) == null, "待删除的角色记录不存在");
        AssertUtil.isTrue(roleMapper.delete(roleId) < 1, "执行删除操作失败");
    }

    /**
     * 角色授权
     * <p>
     * 将对应的角色ID与资源ID，添加到对应的权限表中
     * 直接添加权限：不合适，会出现重复的权限数据（执行修改权限操作后删除权限操作时）
     * 推荐使用：
     * 先将已有的权限记录删除，再将需要设置的权限记录添加
     * 1. 通过角色ID查询对应的权限记录
     * 2. 如果权限记录存在，则删除对应的角色拥有的权限记录
     * 3. 如果有权限记录，则添加权限记录 (批量添加)
     *
     * @param roleId
     * @param mIds
     * @return void
     */
    @Transactional
    public void addGrant(Integer roleId, Integer[] mIds) {
        //通过roleId查询对应的权限记录
        Integer roleCount = permissionMapper.selectRoleCountByPrimaryKey(roleId);
        //如果该role有权限，全部删除(通过roleId)
        if (roleCount > 0) {
            permissionMapper.deleteByRoleId(roleId);
        }
        //如果mIds不为空，添加角色权限
        if (mIds != null) {
            List<Permission> list = new ArrayList<>();
            for (Integer mId : mIds) {
                Permission permission = new Permission();
                //设置值
                permission.setRoleId(roleId);
                permission.setModuleId(mId);
                //设置权限码
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                permission.setCreateDate(new Date());
                permission.setCreateDate(new Date());
                list.add(permission);
            }
            //执行授权操作
            AssertUtil.isTrue(permissionMapper.insertBatch(list) != list.size(), "授权操作执行失败");
        }
    }
}
