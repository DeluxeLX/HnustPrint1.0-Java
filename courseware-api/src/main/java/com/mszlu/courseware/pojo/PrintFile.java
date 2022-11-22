package com.mszlu.courseware.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("print_file")
public class PrintFile {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String filename;

    private String typeImg;

    private String paperStyle;

    private Integer pages;

    private String singleOrDouble;

    private String color;

    private Integer number;

    private Double amount;

    private String orderId;

    // 上传方式，微信0，本地1
    private Integer uploadStyle;

}
