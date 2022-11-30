package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.model.TreeModuleVo;
import com.zhang.crm.vo.Module;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 资源接口
 */
@Mapper
@Component
public interface ModuleMapper extends BaseMapper<Module, Integer> {

    /**
     * 查询所有的资源列表
     * @return
     */
    public List<TreeModuleVo> queryAllModules();

    /**
     * 查询所有的资源数据
     * @return
     */
    List<Module> queryModuleList();

    /**
     * 通过模块名称查询同一级目录下的模块
     * @param moduleName
     * @param grade
     * @return
     */
    Module selectSameGradeByModuleName(String moduleName, Integer grade);

    /**
     * 通过模块Url查询同一级目录下的模块
     * @param url
     * @param grade
     * @return
     */
    Module selectSameGradeByModuleUrl(String url, Integer grade);

    /**
     * 通过权限码查询模块
     * @param optValue
     * @return
     */
    Module selectByOptValue(String optValue);

    /**
     * 通过id查询资源数量
     * @param id
     * @return
     */
    Integer selectByParentId(Integer id);
}