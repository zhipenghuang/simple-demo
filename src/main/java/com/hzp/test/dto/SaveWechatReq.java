package com.hzp.test.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SaveWechatReq {

    @ApiModelProperty(value = "组名",required = true)
    private String group;

    @ApiModelProperty(value = "微信号，逗号隔开",required = true)
    private String wechats;
}
