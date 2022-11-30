package com.zhang.crm.dao;
import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.query.CustomerQuery;
import com.zhang.crm.vo.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * 客户信息接口
 */
@Mapper
@Component
public interface CustomerMapper extends BaseMapper<Customer, Integer> {

    Customer selectByCustomerByName(String name);

    List<Customer> selectAllLossCustomer();

    /**
     *  批量更新客户的流失状态
     * @param lossCustomerIds
     * @return
     */
    int updateCustomerStateByIds(List<Integer> lossCustomerIds);

    /**
     * 查询客户贡献数据
     * @param customerQuery
     * @return
     */
    List<Map<String, Object>> queryCustomerContributionByParams(CustomerQuery customerQuery);

    /**
     * 查询客户构成
     * @return
     */
    List<Map<String, Object>> countCustomerMake();
}