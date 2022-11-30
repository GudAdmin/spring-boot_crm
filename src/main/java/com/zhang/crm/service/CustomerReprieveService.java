package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.CustomerLossMapper;
import com.zhang.crm.dao.CustomerReprieveMapper;
import com.zhang.crm.query.CustomerReprieveQuery;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.vo.CustomerReprieve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerReprieveService extends BaseService<CustomerReprieve, Integer> {
    @Autowired
    private CustomerReprieveMapper customerReprieveMapper;

    @Autowired
    private CustomerLossMapper customerLossMapper;

    public Map<String, Object> list(CustomerReprieveQuery customerReprieveQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerReprieveQuery.getPage(), customerReprieveQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<CustomerReprieve> pageInfo = new PageInfo<>(customerReprieveMapper.selectByParams(customerReprieveQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加暂缓措施
     *
     * @param customerReprieve
     */
    @Transactional
    public void add(CustomerReprieve customerReprieve) {
        //参数判断
        AssertUtil.isTrue(customerReprieve.getMeasure() == null, "暂缓措施不能为空");
        AssertUtil.isTrue(customerLossMapper.selectByPrimaryKey(customerReprieve.getLossId()) == null, "待添加暂缓记录的流失客户不存在");
        //设置默认值
        customerReprieve.setIsValid(1);
        customerReprieve.setCreateDate(new Date());
        customerReprieve.setUpdateDate(new Date());
        //执行添加操作，判断受到影响的行数
        AssertUtil.isTrue(customerReprieveMapper.insertSelective(customerReprieve) < 1, "添加暂缓措施失败");
    }

    /**
     * 修改暂缓措施操作
     *
     * @param customerReprieve
     */
    @Transactional
    public void update(CustomerReprieve customerReprieve) {
        //参数校验
        AssertUtil.isTrue(customerReprieve.getMeasure() == null, "暂缓措施不能为空");
        AssertUtil.isTrue(customerLossMapper.selectByPrimaryKey(customerReprieve.getLossId()) == null, "待添加暂缓记录的流失客户不存在");
        AssertUtil.isTrue(customerReprieveMapper.selectByPrimaryKey(customerReprieve.getId()) == null, "待修改的暂缓记录不存在");
        //设置默认值
        customerReprieve.setUpdateDate(new Date());
        //执行修改操作
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "修改暂缓操作失败");
    }

    /**
     * 删除暂缓措施操作
     *
     * @param id
     */
    @Transactional
    public void delete(Integer id) {
        AssertUtil.isTrue(id == null, "待删除的暂缓措施不存在");
        CustomerReprieve customerReprieve = customerReprieveMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customerReprieve == null, "待删除的暂缓措施不存在");
        //设置值
        customerReprieve.setIsValid(0);
        customerReprieve.setUpdateDate(new Date());
        //执行删除操作
        AssertUtil.isTrue(customerReprieveMapper.updateByPrimaryKeySelective(customerReprieve) < 1, "删除暂缓措施成功");

    }
}
