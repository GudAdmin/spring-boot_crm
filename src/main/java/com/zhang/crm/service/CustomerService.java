package com.zhang.crm.service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.CustomerLossMapper;
import com.zhang.crm.dao.CustomerMapper;
import com.zhang.crm.dao.CustomerOrderMapper;
import com.zhang.crm.query.CustomerQuery;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.utils.PhoneUtil;
import com.zhang.crm.vo.Customer;
import com.zhang.crm.vo.CustomerLoss;
import com.zhang.crm.vo.CustomerOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CustomerService extends BaseService<Customer, Integer> {
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerLossMapper customerLossMapper;

    @Autowired
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 多条件分页查询
     * 返回的数据格式必须满足layUi数据表格要求的格式
     *
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryCustomerQuery(CustomerQuery customerQuery) {
        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<Customer> pageInfo = new PageInfo<>(customerMapper.selectByParams(customerQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加客户
     * 1. 参数校验
     * 客户名称 name
     * 非空，名称唯一
     * 法人代表 fr
     * 非空
     * 手机号码 phone
     * 非空，格式正确
     * 2. 设置参数的默认值
     * 是否有效 isValid    1
     * 创建时间 createDate 系统当前时间
     * 修改时间 updateDate 系统当前时间
     * 流失状态 state      0
     * 0=正常客户  1=流失客户
     * 客户编号 khno
     * 系统生成，唯一 （uuid | 时间戳 | 年月日时分秒 | 雪花算法）
     * 格式：KH + 时间戳
     * 3. 执行添加操作，判断受影响的行数
     *
     * @param customer
     * @return void
     */
    @Transactional
    public void add(Customer customer) {
        //1.参数校验
        checkAddCustomerParams(customer);
        //2.设置默认值
        Customer customer1 = setDefaultValue(customer);
        //3.执行添加操作
        AssertUtil.isTrue(customerMapper.insertSelective(customer) != 1, "顾客添加操作失败");
    }

    /*  1. 参数校验
     *      客户名称 name
     *          非空，名称唯一
     *      法人代表 fr
     *          非空
     *      手机号码 phone
     *          非空，格式正确
     */
    private void checkAddCustomerParams(Customer customer) {
        AssertUtil.isTrue(StringUtils.isBlank(customer.getName()), "用户名称不能为空");
        AssertUtil.isTrue(customerMapper.selectByCustomerByName(customer.getName()) != null, "该顾客名称已经存在");
        AssertUtil.isTrue(StringUtils.isBlank(customer.getFr()), "法人代表不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customer.getPhone()) || !PhoneUtil.isMobile(customer.getPhone()),
                "手机号码不能为空或手机号码格式不正确");
    }

    /*
     *  2. 设置参数的默认值
     *      是否有效 isValid    1
     *      创建时间 createDate 系统当前时间
     *      修改时间 updateDate 系统当前时间
     *      流失状态 state      0
     *          0=正常客户  1=流失客户
     *      客户编号 khno:KH + 时间戳
     */
    private Customer setDefaultValue(Customer customer) {
        customer.setIsValid(1);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setState(0);
        String khno = "KH" + System.currentTimeMillis();
        customer.setKhno(khno);
        return customer;
    }


    /**
     * 修改客户
     * 1. 参数校验
     * 客户ID id
     * 非空，数据存在
     * 客户名称 name
     * 非空，名称唯一
     * 法人代表 fr
     * 非空
     * 手机号码 phone
     * 非空，格式正确
     * 2. 设置参数的默认值
     * 修改时间 updateDate 系统当前时间
     * 3. 执行更新操作，判断受影响的行数
     *
     * @param customer
     * @return void
     */
    @Transactional
    public void update(Customer customer) {
        //1. 参数校验
        AssertUtil.isTrue(customer.getId() == null || customerMapper.selectByPrimaryKey(customer.getId()) == null,
                "待修改的记录不存在");
        AssertUtil.isTrue(customer.getName() == null, "客户名称不能为空");
        Customer temp = customerMapper.selectByCustomerByName(customer.getName());
        if (temp != null) {
            AssertUtil.isTrue(!temp.getId().equals(customer.getId()), "该客户名称已经存在");
        }
        AssertUtil.isTrue(StringUtils.isBlank(customer.getFr()), "法人代表不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(customer.getPhone()) || !PhoneUtil.isMobile(customer.getPhone()),
                "手机号码不能为空或手机号码格式不正确");
        //2.设置默认值
        customer.setUpdateDate(new Date());
        //3.执行修改操作，判断返回的参数
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1, "客户修改操作失败");
    }

    /**
     * 删除客户
     * 1. 参数校验
     * id
     * 非空，数据存在
     * 2. 设置参数默认值
     * isValid     0
     * updateDate  系统当前时间
     * 3. 执行删除（更新）操作，判断受影响的行数
     *
     * @param id
     * @return void
     */
    @Transactional
    public void delete(Integer id) {
        //1.参数校验
        AssertUtil.isTrue(id == null, "待删除数据不存在");
        Customer customer = customerMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(customer == null, "待删除数据不存在");
        //2.设置参数默认值
        customer.setIsValid(0);
        customer.setUpdateDate(new Date());
        //3.执行删除
        AssertUtil.isTrue(customerMapper.updateByPrimaryKeySelective(customer) < 1, "删除顾客操作失败");
    }

    /**
     * 定时任务，将流失的客户转移到流失客户记录表中
     * <p>
     * 更新客户的流失状态
     * 1. 查询待流失的客户数据
     * 2. 将流失客户数据批量添加到客户流失表中
     * 3. 批量更新客户的流失状态  0=正常客户  1=流失客户
     */
    @Transactional
    public void updateCustomerState() {
        //查询待流失的客户数据
        List<Customer> customers = customerMapper.selectAllLossCustomer();
        //如果有数据
        if (customers != null && customers.size() > 0) {
            // 接收流失客户的ID
            List<Integer> LossCustomerIds = new ArrayList<>();
            //接收流失客户
            List<CustomerLoss> LossCustomers = new ArrayList<>();
            //遍历流失客户，加入到流失客户表中
            for (int i = 0; i < customers.size(); i++) {
                //定义流失客户对象
                CustomerLoss customerLoss = new CustomerLoss();
                //设置值
                customerLoss.setCreateDate(new Date());
                //客户经理
                customerLoss.setCusManager(customers.get(i).getCusManager());
                //客户名称
                customerLoss.setCusName(customers.get(i).getName());
                //客户编号
                customerLoss.setCusNo(customers.get(i).getKhno());
                //设置有效
                customerLoss.setIsValid(1);
                customerLoss.setUpdateDate(new Date());
                //流失状态 1 = 确认流失 0 =暂缓流失
                customerLoss.setState(0);

                //设置最后一单的订单时间
                //先查询最后一单时间
                CustomerOrder customerOrder = customerOrderMapper.queryCustomerLastOrderByCustomerId(customers.get(i).getId());
                if (customerOrder != null) {
                    customerLoss.setLastOrderTime(customerOrder.getOrderDate());
                }
                LossCustomers.add(customerLoss);
                LossCustomerIds.add(customers.get(i).getId());
            }
            // 批量添加流失客户记录
            AssertUtil.isTrue(customerLossMapper.insertBatch(LossCustomers) != LossCustomers.size(), "客户流失数据转移失败！");

            /* 3. 批量更新客户的流失状态 */
            AssertUtil.isTrue(customerMapper.updateCustomerStateByIds(LossCustomerIds) != LossCustomerIds.size(), "客户流失数据转移失败！");
        }
    }

    /**
     * 查询客户贡献
     *
     * @param customerQuery
     * @return
     */
    public Map<String, Object> queryCustomerContributionByParams(CustomerQuery customerQuery) {
        Map<String, Object> map = new HashMap<>();

        //开启分页
        PageHelper.startPage(customerQuery.getPage(), customerQuery.getLimit());
        //得到分页对象(对CusDevPlanQuery对象进行分页）
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(customerMapper.queryCustomerContributionByParams(customerQuery));

        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 查询客户构成 （折线图）
     * @return
     */
    public Map<String, Object> countCustomerMake() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String, Object>> dataList = customerMapper.countCustomerMake();
        // 折线图X轴数据  数组
        List<String> data1 = new ArrayList<>();
        // 折线图Y轴数据  数组
        List<Integer> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                data1.add(dataList.get(i).get("level").toString());
                data2.add(Integer.parseInt(dataList.get(i).get("total").toString()));
            }
        }
        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1", data1);
        map.put("data2", data2);

        return map;
    }

    /**
     * 饼状图
     * @return
     */
    public Map<String, Object> countCustomerMake02() {
        Map<String, Object> map = new HashMap<>();
        // 查询客户构成数据的列表
        List<Map<String, Object>> dataList = customerMapper.countCustomerMake();
        // 饼状图数据   数组（数组中是字符串）
        List<String> data1 = new ArrayList<>();
        // 饼状图的数据  数组（数组中是对象）
        List<Map<String, Object>> data2 = new ArrayList<>();

        // 判断数据列表 循环设置数据
        if (dataList != null && dataList.size() > 0) {
            // 遍历集合
            for (int i = 0; i < dataList.size(); i++) {
                //饼状图数据， 数组（数组中是字符串
                data1.add(dataList.get(i).get("level").toString());
                //饼状图数据 数组（数组中是对象）
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("name", dataList.get(i).get("level"));
                dataMap.put("value", dataList.get(i).get("total"));
                data2.add(dataMap);
            }
        }

        // 将X轴的数据集合与Y轴的数据集合，设置到map中
        map.put("data1", data1);
        map.put("data2", data2);

        return map;
    }
}
