package com.hzp.test.dto;

import com.hzp.test.dto.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class PageWechatReq extends PageRequest {

    @ApiModelProperty("组名")
    private String group;

    @ApiModelProperty("微信号")
    private String wechats;
}
