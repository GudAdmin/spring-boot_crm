package com.zhang.crm.service;

import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.ModuleMapper;
import com.zhang.crm.dao.PermissionMapper;
import com.zhang.crm.model.TreeModuleVo;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module, Integer> {
    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的角色资源
     *
     * @return
     */
    public List<TreeModuleVo> queryAllModules(Integer roleId) {
        //查询所有的资源
        List<TreeModuleVo> treeModuleVoList = moduleMapper.queryAllModules();
        //通过roleId查询该角色拥有的资源ID
        List<Integer> ids = permissionMapper.queryRoleModuleIdsByRoleId(roleId);
        //如果ids不为空，说明拥有资源，遍历treeModuleVolist,为其checked设置值
        for (int i = 0; i < treeModuleVoList.size(); i++) {
            if (ids.contains(treeModuleVoList.get(i).getId())) {
                treeModuleVoList.get(i).setChecked(true);
            }
        }
        return treeModuleVoList;
    }

    /**
     * 查询资源数据
     *
     * @return
     */
    public Map<String, Object> queryModuleList() {
        Map<String, Object> map = new HashMap<>();
        //查询资源列表
        List<Module> moduleList = moduleMapper.queryModuleList();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", moduleList.size());
        map.put("data", moduleList);
        return map;
    }

    /**
     * 添加资源
     * 1. 参数校验
     * 模块名称 moduleName
     * 非空，同一层级下模块名称唯一
     * 地址 url
     * 二级菜单（grade=1），非空且同一层级下不可重复
     * 父级菜单 parentId
     * 一级菜单（目录 grade=0）    -1
     * 二级|三级菜单（菜单|按钮 grade=1或2）    非空，父级菜单必须存在
     * 层级 grade
     * 非空，0|1|2
     * 权限码 optValue
     * 非空，不可重复
     * 2. 设置参数的默认值
     * 是否有效 isValid    1
     * 创建时间createDate  系统当前时间
     * 修改时间updateDate  系统当前时间
     * 3. 执行添加操作，判断受影响的行数
     *
     * @param module
     * @return void
     */
    @Transactional
    public void addModule(Module module) {
        //1.参数校验
        Module newModule = checkAddModule(module);
        //2.设置参数的默认值
        module.setIsValid((byte) 1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        //3.执行添加操作，判断受影响的行数
        AssertUtil.isTrue(moduleMapper.insertSelective(module) != 1, "添加资源操作失败");
    }

    /**
     * 1. 参数校验
     * 模块名称 moduleName
     * 非空，同一层级下模块名称唯一
     * 地址 url
     * 二级菜单（grade=1），非空且同一层级下不可重复
     * 父级菜单 parentId
     * 一级菜单（目录 grade=0）    -1
     * 二级|三级菜单（菜单|按钮 grade=1或2）    非空，父级菜单必须存在
     * 层级 grade
     * 非空，0|1|2
     * 权限码 optValue
     * 非空，不可重复
     */
    private Module checkAddModule(Module module) {
        Integer grade = module.getGrade();
        //1.模块名称
        AssertUtil.isTrue(module.getModuleName() == null, "模块名称不能为空");
        AssertUtil.isTrue(moduleMapper.selectSameGradeByModuleName(module.getModuleName(), grade) != null, "该模块已在相同层级下存在");
        //2.url
        if (grade == 1) {
            AssertUtil.isTrue(module.getUrl() == null, "第二级目录下的url地址不能为空");
            AssertUtil.isTrue(moduleMapper.selectSameGradeByModuleUrl(module.getUrl(), grade) != null, "第二级目录下该Url地址已经存在");
        }
        //3.层级grade
        AssertUtil.isTrue(grade == null || !(grade == 0 || grade == 1 || grade == 2), "添加的模块的层级目录不合法");
        AssertUtil.isTrue(StringUtils.isBlank(module.getOptValue()), "权限码不能为空");
        AssertUtil.isTrue(moduleMapper.selectByOptValue(module.getOptValue()) != null, "该权限码已经存在");

        //4.父级菜单
        if (grade == 0) {
            module.setParentId(-1);
        } else {
            AssertUtil.isTrue(module.getParentId() == null, "父级菜单不能为空");
            Module parent = moduleMapper.selectByPrimaryKey(module.getParentId());
            AssertUtil.isTrue(parent == null, "父级菜单不存在");
            module.setParentOptValue(parent.getOptValue());
        }
        return module;
    }

    /**
     * 修改资源
     * 1. 参数校验
     * id
     * 非空，数据存在
     * 层级 grade
     * 非空 0|1|2
     * 模块名称 moduleName
     * 非空，同一层级下模块名称唯一 （不包含当前修改记录本身）
     * 地址 url
     * 二级菜单（grade=1），非空且同一层级下不可重复（不包含当前修改记录本身）
     * 权限码 optValue
     * 非空，不可重复（不包含当前修改记录本身）
     * 2. 设置参数的默认值
     * 修改时间updateDate  系统当前时间
     * 3. 执行更新操作，判断受影响的行数
     *
     * @param module
     * @return void
     */
    @Transactional
    public void update(Module module) {
        //1.参数校验
        checkUpdateModule(module);
        //2.设置参数的默认值
        module.setUpdateDate(new Date());
        //3.执行操作，判断受到影响的行数
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(module) < 1, "修改模块操作失败");
    }

    /**
     * 修改资源
     * 1. 参数校验
     * id
     * 非空，数据存在
     * 层级 grade
     * 非空 0|1|2
     * 模块名称 moduleName
     * 非空，同一层级下模块名称唯一 （不包含当前修改记录本身）
     * 地址 url
     * 二级菜单（grade=1），非空且同一层级下不可重复（不包含当前修改记录本身）
     * 权限码 optValue
     * 非空，不可重复（不包含当前修改记录本身）
     */
    private void checkUpdateModule(Module module) {
        Integer grade = module.getGrade();
        AssertUtil.isTrue(module.getId() == null || moduleMapper.selectByPrimaryKey(module.getId()) == null, "待修改记录不存在");
        AssertUtil.isTrue(grade == null || !(grade == 1 || grade == 2 || grade == 0), "待修改的层级不合法");
        AssertUtil.isTrue(module.getModuleName() == null ||
                        !moduleMapper.selectSameGradeByModuleName(module.getModuleName(), grade).getId().equals(module.getId()),
                "该模块名在同级目录下已经存在");
        if (grade == 1) {
            AssertUtil.isTrue(module.getUrl() == null ||
                    !moduleMapper.selectSameGradeByModuleUrl(module.getUrl(), grade).getId().equals(module.getId()), "该url地址在同级目录下已经存在");
        }
        AssertUtil.isTrue(module.getOptValue() == null ||
                        !moduleMapper.selectByOptValue(module.getOptValue()).getId().equals(module.getId())
                , "该权限码已经存在");
    }


    /**
     * 删除资源
     * 1. 判断删除的记录是否存在
     * 2. 如果当前资源存在子记录，则不可删除
     * 3. 删除资源时，将对应的权限表的记录也删除（判断权限表中是否存在关联数据，如果存在，则删除）
     * 4. 执行删除（更新）操作，判断受影响的行数
     */
    @Transactional
    public void delete(Integer id) {
        Module temp = moduleMapper.selectByPrimaryKey(id);
        //判断删除的记录是否存在
        AssertUtil.isTrue(id == null || temp == null, "待删除记录不存在");
        Integer moduleCount = moduleMapper.selectByParentId(id);
        AssertUtil.isTrue(moduleCount == null || moduleCount != 0, "当前资源存在子记录，不可删除");

        //删除资源时，将对应的权限表的记录也删除（判断权限表中是否存在关联数据，如果存在，则删除）
        Integer permissionCount = permissionMapper.selectByModuleId(id);
        if (permissionCount > 0) {
            AssertUtil.isTrue(permissionMapper.deleteByModuleId(id) < 1, "删除模块时，删除对应的权限资源失败");
        }
        temp.setIsValid((byte) 0);
        temp.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleMapper.updateByPrimaryKeySelective(temp) < 1, "删除模块操作失败");
    }
}
