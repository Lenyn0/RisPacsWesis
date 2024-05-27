package com.by.ris_springboot.settings.domain;

import lombok.Data;

@Data // lombok插件注解，自动生成getter、setter、toString方法
public class DicValue {

    private String id;
    private String value;
    private String text;
    private String orderNo;
    private String typeCode;
}
