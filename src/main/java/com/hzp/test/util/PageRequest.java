package com.hzp.test.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PageRequest {

    @ApiModelProperty(value = "当前页码，从1开始，默认为1", required = true)
    private Integer pageIndex = 1;

    @ApiModelProperty(value = "每页多少条记录,默认为10", required = true)
    private Integer pageSize = 10;

}

