package com.zhang.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.crm.base.BaseService;
import com.zhang.crm.dao.UserMapper;
import com.zhang.crm.dao.UserRoleMapper;
import com.zhang.crm.model.UserModel;
import com.zhang.crm.query.UserQuery;
import com.zhang.crm.utils.AssertUtil;
import com.zhang.crm.utils.Md5Util;
import com.zhang.crm.utils.PhoneUtil;
import com.zhang.crm.utils.UserIDBase64;
import com.zhang.crm.vo.User;
import com.zhang.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional
    public void updatePassWord(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        //通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户对象是否存在
        AssertUtil.isTrue(user == null, "待更新记录不存在");

        //参数校验
        checkPasswordParams(user, oldPwd, newPwd, repeatPwd);
        //设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        //执行更新，判断收到影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改密码失败");
    }

    /**
     * 修改密码的参数校验
     *
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
        //判断传入的原始密码是非为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "原始密码不能为空");
        //判断原始密码是否正确(先md5加密再比较）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "原始密码不正确");
        //判断新密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空");
        //比较旧密码和新密码是否相同
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码不能和原始密码相同");
        //判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd), "确认密码不能为空");
        //判断确认密码和新密码是否一致
        AssertUtil.isTrue(!newPwd.equals(repeatPwd), "新密码和确认密码不一致");

    }


    /**
     * 用户登录
     *
     * @param username
     * @param password
     */
    public UserModel userLogin(String username, String password) {
        //1.参数判断，判断用户名，密码非空
        checkLoginParams(username, password);

        //2.通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryByName(username);

        //3.判断用户对象是否为空
        AssertUtil.isTrue(user == null, "用户姓名不存在");

        //4.判断密码是否正确，密码是通过md5加密过的，要先对输入进来的密码进行md5加密再比较
        checkUserPassword(password, user.getUserPwd());

        //5.返回构建的用户对象
        return bulidUserInfo(user);
    }

    //构建需要返回给客户端的用户对象
    private UserModel bulidUserInfo(User user) {
        UserModel userVo = new UserModel();
        //userVo.setUserId(user.getId());
        //设置加密后的ID
        userVo.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userVo.setUserName(user.getUserName());
        userVo.setTrueName(user.getTrueName());
        return userVo;
    }

    //密码判断,先将客户端传过来的密码进行加密，再进行比较
    private void checkUserPassword(String password, String userPwd) {
        //将客户端传过来的密码加密
        password = Md5Util.encode(password);
        AssertUtil.isTrue(!password.equals(userPwd), "用户密码不正确");
    }

    //参数判断
    private void checkLoginParams(String username, String password) {
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(username), "用户姓名不能为空");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(password), "密码不能为空");
    }

    //查询所有的销售人员
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }

    /**
     * 查询所有的用户
     *
     * @param userQuery
     * @return
     */
    public Map<String, Object> queryUserByParams(UserQuery userQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(userQuery.getPage(), userQuery.getLimit());
        //得到分页对象(对SaleChance对象进行分页）
        PageInfo<User> pageInfo = new PageInfo<>(userMapper.selectByParams(userQuery));
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加用户操作
     * 1. 参数校验
     *     用户名 非空 唯一性
     *     邮箱   非空
     *     手机号 非空 格式合法
     * 2. 设置默认参数
     *     isValid 1
     *     creteDate   当前时间
     *     updateDate 当前时间
     *     userPwd 123456 -> md5加密
     * 3. 执行添加，判断结果
     */
    @Transactional
    public void addUser(User user) {
        //参数校验
        checkAddUserParms(user.getUserName(), user.getEmail(), user.getPhone());
        //设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        //执行添加，判断结果
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1, "添加用户操作失败");
        /**
         * 添加用户对应角色关系
         */
        //userId数据库自动设置，需要通过唯一字段userName获取user，再获取userId
        Integer userId = userMapper.selectByUserName(user.getUserName()).getId();
        relationUserRole(userId, user.getRoleIds());
    }

    //参数校验
    private void checkAddUserParms(String userName, String email, String phone) {
        //参数校验
        //用户名 非空 唯一性
        AssertUtil.isTrue(userName == null || userName == "", "用户名不能为空");
        User temp = userMapper.selectByUserName(userName);
        AssertUtil.isTrue(temp != null, "该用户已经存在");
        //邮箱   非空
        AssertUtil.isTrue(email == null || email == "", "邮箱不能为空");
        //机号 非空 格式合法
        AssertUtil.isTrue(phone == null || !PhoneUtil.isMobile(phone), "手机号不能为空或格式不正确");
    }


    /**
     * 更新用户操作
     * 1. 参数校验
     *    id 非空 记录必须存在
     *    用户名 非空 唯一性
     *     email 非空
     *    手机号 非空 格式合法
     * 2. 设置默认参数
     *     updateDate
     * 3. 执行更新，判断结果
     *
     * @param user
     */
    @Transactional
    public void updateUser(User user) {
        //参数校验
        checkUpdateUserParms(user.getUserName(), user.getEmail(), user.getPhone(), user.getId());
        //设置默认值
        user.setUpdateDate(new Date());
        //执行修改，判断返回影响行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1, "用户修改操作失败");
        /**
         * 修改用户对应角色关系
         */
        relationUserRole(user.getId(), user.getRoleIds());
    }

    //用户修改操纵参数校验

    /**
     * 1. 参数校验
     * id 非空 记录必须存在
     * 用户名 非空 唯一性
     * email 非空
     * 手机号 非空 格式合法
     *
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUpdateUserParms(String userName, String email, String phone, Integer id) {
        AssertUtil.isTrue(id == null, "待修改记录不存在");
        User user = userMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(user == null, "待修改记录不存在");
        AssertUtil.isTrue(userName == null, "用户名不能为空");
        User temp = userMapper.selectByUserName(userName);
        AssertUtil.isTrue(temp != null && !temp.getId().equals(id), "该用户名已经存在");
        AssertUtil.isTrue(email == null, "邮箱不能为空");
        AssertUtil.isTrue(phone == null || !PhoneUtil.isMobile(phone), "手机号不能为空或格式不正确");
    }

    /**
     * 删除用户操作
     *
     * @param ids
     */
    @Transactional
    public void deleteUserByIds(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "待删除的用户不存在");
        AssertUtil.isTrue(userMapper.deleteBatch(ids) < 1, "删除用户操作失败");
        /**
         * 删除用户对应角色关系
         */
        for (Integer id : ids) {
            if (userRoleMapper.queryUserRolesCountById(id) > 0) {
                AssertUtil.isTrue(userRoleMapper.deleteAllRolesByUserId(id) < 1, "删除用户角色失败");
            }
        }
    }

    /**
     * 用户角色关联
     * 添加操作
     * 原始角色不存在
     * 1. 不添加新的角色记录    不操作用户角色表
     * 2. 添加新的角色记录      给指定用户绑定相关的角色记录
     * <p>
     * 更新操作
     * 原始角色不存在
     * 1. 不添加新的角色记录     不操作用户角色表
     * 2. 添加新的角色记录       给指定用户绑定相关的角色记录
     * 原始角色存在
     * 1. 添加新的角色记录       判断已有的角色记录不添加，添加没有的角色记录
     * 2. 清空所有的角色记录     删除用户绑定角色记录
     * 3. 移除部分角色记录       删除不存在的角色记录，存在的角色记录保留
     * 4. 移除部分角色，添加新的角色    删除不存在的角色记录，存在的角色记录保留，添加新的角色
     * <p>
     * 如何进行角色分配？？？
     * 判断用户对应的角色记录存在，先将用户原有的角色记录删除，再添加新的角色记录
     * <p>
     * 删除操作
     * 删除指定用户绑定的角色记录
     *
     * @param userId  用户ID
     * @param roleIds 角色ID
     * @return
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //通过 userId查询用户的角色记录
        Integer userIdCount = userRoleMapper.queryUserRolesCountById(userId);
        // 判断userIdCount是否大于0，如果大于0，通过userId删除t_user_role所有和userId绑定的角色
        if (userIdCount > 0) {
            userRoleMapper.deleteAllRolesByUserId(userId);
        }
        //判断roleIds，如果有值，在t_user_role中添加用户对应的角色记录
        List<UserRole> userRoleList = new ArrayList<>();
        if (roleIds != null && roleIds != "") {
            String[] temp = roleIds.split(",");
            for (String arr : temp) {
                UserRole userRole = new UserRole();
                Integer roleId = Integer.parseInt(arr);
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoleList.add(userRole);
            }
        }
        //执行添加操作，判断受影响的行数
        AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(), "执行失败");
    }

    /**
     * 查询所有的客户经理
     *
     * @return
     */
    public List<Map<String, Object>> queryAllCustomerManagers() {
        return userMapper.queryAllCustomerManagers();
    }
}
