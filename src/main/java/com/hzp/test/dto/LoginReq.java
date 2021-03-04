package com.hzp.test.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class LoginReq {

    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
