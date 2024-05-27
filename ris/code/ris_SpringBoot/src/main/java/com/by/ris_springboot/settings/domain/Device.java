package com.by.ris_springboot.settings.domain;

import lombok.Data;

@Data
public class Device {
    private String id;
    private String name;
    private String aet;
    private String port;
    private String ip;
    private String room;
}
