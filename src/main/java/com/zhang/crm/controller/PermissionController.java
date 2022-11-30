package com.zhang.crm.controller;
import com.zhang.crm.base.BaseController;
import com.zhang.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 权限控制层
 */
@Controller
public class PermissionController extends BaseController {
    @Autowired
    private PermissionService permissionService;

}
