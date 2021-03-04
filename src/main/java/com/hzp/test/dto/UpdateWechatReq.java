package com.hzp.test.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UpdateWechatReq {

    @ApiModelProperty(value = "id",required = true)
    private Integer id;

    @ApiModelProperty("组名")
    private String group;

    @ApiModelProperty("微信号，逗号隔开")
    private String wechats;
}
