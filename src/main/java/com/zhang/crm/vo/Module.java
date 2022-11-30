package com.zhang.crm.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 资源实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module implements Serializable {
    private Integer id;
    private String moduleName;
    private String moduleStyle;
    private String url;
    private Integer parentId;
    private String parentOptValue;
    private Integer grade;
    private String optValue;
    private Integer orders;
    private Byte isValid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 如果传递的参数是Date类型，要求传入的时间字符串的格式
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 如果传递的参数是Date类型，要求传入的时间字符串的格式
    private Date updateDate;
}