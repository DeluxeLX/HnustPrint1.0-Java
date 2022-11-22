package com.mszlu.courseware.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalFileDto {

    private String filename;

    private Integer pages;

    private String url;

}
