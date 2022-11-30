package com.zhang.crm.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 定义方法需要的对应资源的权限码
 * 实现了菜单级别显示控制，但最终客户端有可能会通过浏览器来输入资源地址从而越过ui界面来访问后
 * 端资源，所以接下来加入控制方法级别资源的访问控制操作，这里使用aop+自定义注解实现
 */
public @interface RequiredPermission {
    //权限码
    String code() default "";
}
