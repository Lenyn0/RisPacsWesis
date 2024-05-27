package com.by.ris_springboot.settings.domain;

import lombok.Data;

@Data
public class DiseaseDictionary {

    private String id;
    private String bodyPart;
    private String name;
    private String description;
    private String createUserID;
    private String createTime;
    private String modifyTime;
    private String modifyUserID;

}
