package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class PageVo {

    private List rows;
    private Long total;

    public PageVo(List rows, Long total) {

        this.setRows(rows);
        this.setTotal(total);

    }
}