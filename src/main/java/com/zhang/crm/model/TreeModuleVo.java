package com.zhang.crm.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 权限资源的树形显示模型
 */
public class TreeModuleVo {
    private Integer id;
    private Integer pId;
    private String name;
    private boolean checked = false;
}
