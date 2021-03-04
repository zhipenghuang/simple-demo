package com.hzp.test.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class WechatIdReq {

    @ApiModelProperty(value = "id", required = true)
    private Integer id;
}
