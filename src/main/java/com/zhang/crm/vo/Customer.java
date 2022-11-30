package com.zhang.crm.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * 客户信息表
 */
@Data
public class Customer implements Serializable {
    private Integer id;
    private String khno;
    private String name;
    private String area;
    private String cusManager;
    private String level;
    private String myd;
    private String xyd;
    private String address;
    private String postCode;
    private String phone;
    private String fax;
    private String webSite;
    private String yyzzzch;
    private String fr;
    private String zczj;
    private String nyye;
    private String khyh;
    private String khzh;
    private String dsdjh;
    private String gsdjh;
    private Integer state;
    private Integer isValid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 如果传递的参数是Date类型，要求传入的时间字符串的格式
    private Date createDate;    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 如果传递的参数是Date类型，要求传入的时间字符串的格式
    private Date updateDate;
}