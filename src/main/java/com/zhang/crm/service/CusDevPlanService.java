package com.zhang.crm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.CusDevPlanMapper;
import com.zhang.crm.dao.SaleChanceMapper;
import com.zhang.crm.query.CusDevPlanQuery;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.vo.CusDevPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {
    @Autowired
    private CusDevPlanMapper cusDevPlanMapper;

    @Autowired
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询
     * 返回的数据格式必须满足layUi数据表格要求的格式
     *
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlanParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加计划项
     * 1. 参数校验
     *     营销机会ID 非空 记录必须存在
     *     计划项内容   非空
     *     计划项时间   非空
     * 2. 设置参数默认值
     *     is_valid
     *     crateDate
     *     updateDate
     * 3. 执行添加，判断结果
     */
    @Transactional
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        //参数校验
        checkaddCusDevPlanParms(cusDevPlan);
        //设置默认值
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1, "添加客户开发计划失败");
    }

    /**
     * 1. 参数校验
     * 营销机会ID 非空 记录必须存在
     * 计划项内容   非空
     * 计划项时间   非空
     *
     * @param cusDevPlan
     */
    private void checkaddCusDevPlanParms(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(cusDevPlan.getSaleChanceId() == null || saleChanceMapper.selectByPrimaryKey(cusDevPlan.getSaleChanceId()) == null, "该营销机会ID不存在");
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null, "请设置计划项指定日期");
        AssertUtil.isTrue(cusDevPlan.getPlanItem() == null || cusDevPlan.getPlanItem() == "", "请设置计划项内容");
    }


    /**
     * 更新计划项
     * 1.参数校验
     *     id 非空 记录存在
     *     营销机会id 非空 记录必须存在
     *     计划项内容 非空
     *     计划项时间 非空
     * 2.参数默认值设置
     *     updateDate
     * 3.执行更新 判断结果
     */
    @Transactional
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        checkUpdateCusDevPlan(cusDevPlan);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1, "修改用户开发计划失败");
    }

    private void checkUpdateCusDevPlan(CusDevPlan cusDevPlan) {
        /**
         *1.参数校验
         *id 非空 记录存在
         *营销机会id 非空 记录必须存在
         *计划项内容 非空
         *计划项时间 非空
         */
        AssertUtil.isTrue(cusDevPlan.getId() == null || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null, "该客户开发计划不存在");
        checkaddCusDevPlanParms(cusDevPlan);
    }


    /**
     * 1.参数校验
     * id 非空 记录存在
     *
     * @param cusDevPlan
     */
    @Transactional
    public void deleteCusDevPlan(CusDevPlan cusDevPlan) {
        checkDeleteCusDevPlan(cusDevPlan);
        AssertUtil.isTrue(cusDevPlanMapper.deleteByPrimaryKey(cusDevPlan.getId()) < 1, "删除客户开发项计划失败");
    }

    private void checkDeleteCusDevPlan(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(cusDevPlan.getId() == null || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null, "待删除的客户开发项计划不存在");
    }
}
