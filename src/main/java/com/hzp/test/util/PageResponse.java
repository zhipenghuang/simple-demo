package com.hzp.test.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class PageResponse<T> {

    @ApiModelProperty("页码")
    private int pageIndex;

    @ApiModelProperty("页数")
    private int pageSize;

    @ApiModelProperty("总条数")
    private int total;

    @ApiModelProperty("数据")
    private List<T> data;
}
