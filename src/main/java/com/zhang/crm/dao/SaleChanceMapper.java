package com.zhang.crm.dao;

import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.SaleChance;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 营销机会接口
 */
@Mapper
@Component
public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    /**
     * 通过id查询营销机会
     * @param id
     * @return
     */
    Integer deleteByPrimaryKey(Integer id);

    /**
     * 添加营销机会
     * @param record
     * @return
     */
    Integer insert(SaleChance record);

    Integer insertSelective(SaleChance record);

    /**
     * 通过id查询营销机会
     * @param id
     * @return
     */
    SaleChance selectByPrimaryKey(Integer id);

    /**
     * 不为空进行修改
     * @param record
     * @return
     */
    Integer updateByPrimaryKeySelective(SaleChance record);

    /**
     * 修改营销机会
     * @param record
     * @return
     */
    Integer updateByPrimaryKey(SaleChance record);
}