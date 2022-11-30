package com.zhang.crm.query;

import com.zhang.crm.base.BaseQuery;
import lombok.Getter;
import lombok.Setter;

/**
 * 客户服务查询条件
 */
@Getter
@Setter
public class CustomerServeQuery extends BaseQuery {

    private String customer; // 客户名称
    private Integer serveType; // 服务类型
    private String state; // 服务状态  服务创建=fw_001  服务分配=fw_002  服务处理=fw_003  服务反馈=fw_004  服务归档=fw_005

    private Integer assigner; // 分配人
}
