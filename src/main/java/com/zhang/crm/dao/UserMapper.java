package com.zhang.crm.dao;
import com.zhang.crm.base.BaseMapper;
import com.zhang.crm.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 */
@Component
@Mapper
public interface UserMapper extends BaseMapper<User, Integer> {

    /**
     * 通过用户名查询用户记录
     * @param username
     * @return
     */
    User queryByName(String username);

    /**
     * 通过id删除用户信息
     * @param id
     * @return
     */
    Integer deleteByPrimaryKey(Integer id);

    /**
     * 查询所有的销售人员
     * @return
     */
    List<Map<String, Object>> queryAllSales();

    /**
     * 根据用户名查询用户信息
     * @param userName
     * @return
     */
    User selectByUserName(String userName);

    /**
     * 查询所有的客户经理
     * @return
     */
    List<Map<String, Object>> queryAllCustomerManagers();
}