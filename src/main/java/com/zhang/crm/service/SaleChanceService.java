package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.SaleChanceMapper;
import com.zhang.crm.enums.DevResult;
import com.zhang.crm.enums.StateStatus;
import com.zhang.crm.query.SaleChanceQuery;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.utils.PhoneUtil;
import com.zhang.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询
     * 返回的数据格式必须满足layUi数据表格要求的格式
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        //得到分页对象(对SaleChance对象进行分页）
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加营销机会
     * 1. 参数校验
     * customerName客户名称    非空
     * linkMan联系人           非空
     * linkPhone联系号码       非空，手机号码格式正确
     * 2. 设置相关参数的默认值
     * createMan创建人        当前登录用户名
     * assignMan指派人
     * 如果未设置指派人（默认）
     * state分配状态 （0=未分配，1=已分配）
     * 0 = 未分配
     * assignTime指派时间
     * 设置为null
     * devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
     * 0 = 未开发 （默认）
     * 如果设置了指派人
     * state分配状态 （0=未分配，1=已分配）
     * 1 = 已分配
     * assignTime指派时间
     * 系统当前时间
     * devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
     * 1 = 开发中
     * isValid是否有效  （0=无效，1=有效）
     * 设置为有效 1= 有效
     * createDate创建时间
     * 默认是系统当前时间
     * updateDate
     * 默认是系统当前时间
     * 3. 执行添加操作，判断受影响的行数
     */

    @Transactional
    public void addSaleChance(SaleChance saleChance) {
        //1.参数校验
        checkSaleChanceParms(saleChance.getCustomerName(), saleChance.getLinkMan(),
                saleChance.getLinkPhone());
        //2.设置相关属性的默认值
        //设置为有效 1 = 有效
        saleChance.setIsValid(1);
        //createDate默认是当前的系统时间
        saleChance.setCreateDate(new Date());
        //updateDate默认是系统时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            //如果为空,则表示未设置指派人
            //分配状态为未分配
            //state分配状态 （0=未分配，1=已分配） 0 = 未分配
            saleChance.setState(StateStatus.UNSTATE.getType());
            //没有指派人，assignTime未空
            saleChance.setAssignTime(null);
            //开发状态为未开发
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            //如果不为空，则表示设置了指派人
            /**
             * 如果设置了指派人
             *                 state分配状态 （0=未分配，1=已分配）
             *                    1 = 已分配
             *                 assignTime指派时间
             *                    系统当前时间
             *                 devResult开发状态 （0=未开发，1=开发中，2=开发成功，3=开发失败）
             *                    1 = 开发中
             */
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(DevResult.DEVING.getStatus());

        }
        //3. 执行添加操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销机会失败");
    }

    /**
     * 参数校验
     *
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParms(String customerName, String linkMan, String linkPhone) {
        //customerName不能为空
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "用户名称不能为空");
        //linkMan不能为空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        //linkPhone不能为空
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系电话不能为空");
        //判断手机号码的格式是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "联系号码格式不正确");
    }


    /**
     * 更新营销机会
     * 1. 参数校验
     * 营销机会ID  非空，数据库中对应的记录存在
     * customerName客户名称    非空
     * linkMan联系人           非空
     * linkPhone联系号码       非空，手机号码格式正确
     * <p>
     * 2. 设置相关参数的默认值
     * updateDate更新时间  设置为系统当前时间
     * assignMan指派人
     * 原始数据未设置
     * 修改后未设置
     * 不需要操作
     * 修改后已设置
     * assignTime指派时间  设置为系统当前时间
     * 分配状态    1=已分配
     * 开发状态    1=开发中
     * 原始数据已设置
     * 修改后未设置
     * assignTime指派时间  设置为null
     * 分配状态    0=未分配
     * 开发状态    0=未开发
     * 修改后已设置
     * 判断修改前后是否是同一个指派人
     * 如果是，则不需要操作
     * 如果不是，则需要更新 assignTime指派时间  设置为系统当前时间
     * <p>
     * 3. 执行更新操作，判断受影响的行数
     */
    @Transactional
    public void updateSaleChance(SaleChance saleChance) {
        //1.参数校验
        //营销机会ID：非空，且数据库中有对应的数据
        AssertUtil.isTrue(saleChance.getId() == null, "待更新记录不存在");
        //判断数据库中是否存在该对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp == null, "待更新记录不存在");

        //传入的参数校验
        checkSaleChanceParms(saleChance.getCustomerName(), saleChance.getLinkMan(),
                saleChance.getLinkPhone());

        /*设置相关参数的默认值*/
        //updateDate更新时间  设置为系统当前时间
        saleChance.setUpdateDate(new Date());
        //assignMan指派人
        //判断原始数据是否存在
        if (StringUtils.isBlank(temp.getAssignMan())) {//不存在
            //判断修改后的值是否存在
            //开始不存在，修改后也不存在，则不需要操作
            //开始不存在，修改后存在，设置默认值
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {
                // assignTime指派时间  设置为系统当前时间
                saleChance.setAssignTime(new Date());
                //分配状态    1=已分配
                saleChance.setState(StateStatus.STATED.getType());
                //开发状态    1=开发中
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }

        } else {//存在
            if (StringUtils.isBlank(saleChance.getAssignMan())) {
                //修改前有值，修改有无值
                //assignTime指派时间  设置为null
                saleChance.setAssignTime(null);
                //分配状态    0=未分配
                saleChance.setState(StateStatus.UNSTATE.getType());
                //开发状态    0=未开发
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            } else {
                //修改前有值，修改后也有值
                //判断修改前后是否是同一个指派人
                if (!temp.getAssignMan().equals(saleChance.getAssignMan())) {
                    //如果不是，则需要更新 assignTime指派时间  设置为系统当前时间
                    saleChance.setAssignTime(new Date());
                } else {
                    //如果是，使用原来的指派时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        //3. 执行更新操作，判断受影响的行数
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1, "更新营销机会失败");
    }


    /**
     * 批量删除营销机会
     *
     * @param array
     */
    @Transactional
    public void deleteSaleChance(Integer[] array) {
        AssertUtil.isTrue(array == null || array.length == 0, "请选择需要删除的数据");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(array) < 1, "删除营销机会数据失败");
    }


    @Transactional
    /**
     * 更新营销机会开发状态
     */
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        //参数判断，判断营销机会ID是否有效（ID是否存在且数据库中有记录
        AssertUtil.isTrue(id == null, "待更新记录不存在");
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(temp == null, "待更新记录不存在");
        temp.setDevResult(devResult);
        saleChanceMapper.updateByPrimaryKeySelective(temp);
    }
}
