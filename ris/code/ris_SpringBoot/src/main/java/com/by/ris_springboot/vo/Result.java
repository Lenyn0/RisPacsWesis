package com.by.ris_springboot.vo;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;
    private boolean success;
    private String msg;
    private T data;

    public Result() {
        this(true);
    }

    public Result(boolean success) {
        this.success = success;
    }

}
