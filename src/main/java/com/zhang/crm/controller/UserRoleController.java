package com.zhang.crm.controller;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 用户角色控制层
 */
@Controller
public class UserRoleController extends BaseController {
    @Resource
    private UserRoleService userRoleService;
}
