package com.mszlu.courseware.pojo.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mszlu.courseware.pojo.PrintFile;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto implements Serializable {

    @TableId(value = "id")
    private String id;

    private Integer status;

    private Integer userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private Double amount;

    private Integer addressId;

    private Integer documentNum;

    // order 扩展属性, 订单所附带的文件信息
    private List<PrintFile> fileList;

}
