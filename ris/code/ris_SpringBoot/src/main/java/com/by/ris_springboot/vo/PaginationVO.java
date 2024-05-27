package com.by.ris_springboot.vo;

import lombok.Data;

import java.util.List;

@Data
public class PaginationVO<T> {

    private int total;
    private List<T> dataList;

}
