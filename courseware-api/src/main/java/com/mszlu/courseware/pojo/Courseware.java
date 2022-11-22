package com.mszlu.courseware.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *  课程
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Courseware implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @NotNull(message = "姓名不能为空")
    private String name;
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    @Min(value = 0, message = "购买量不能小于0")
    private Integer count;
    @NotNull(message = "课件地址不能为空")
    private String url;
    @NotNull(message = "封面不能为空")
    private String cover;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private String carouselUrl;

    private Integer isCarousel;
}
