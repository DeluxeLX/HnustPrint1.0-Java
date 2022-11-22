package com.mszlu.courseware.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("print_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {

    @TableId(value = "id")
    private String id;

    private Integer status;

    private Integer userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private Double amount;

    private Integer addressId;

    private Integer documentNum;

    private String documentName;
}
