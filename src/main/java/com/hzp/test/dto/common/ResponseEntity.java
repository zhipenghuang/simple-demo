package com.hzp.test.dto.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.hzp.test.exception.Errors;
import com.hzp.test.exception.SystemErrors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yunmu
 * @create 2018/9/19.
 */
@Data
@ApiModel
public class ResponseEntity<T> {

    @ApiModelProperty("code,0成功,其他失败")
    @JSONField(ordinal = 1)
    private int code;

    @ApiModelProperty("请求结果信息提示")
    @JSONField(ordinal = 2)
    private String msg;

    @ApiModelProperty("具体数据")
    @JSONField(ordinal = 3)
    private T data;

    public ResponseEntity() {
        this.code = SystemErrors.SUCCESS.code;
        this.msg = SystemErrors.SUCCESS.message;
    }

    public ResponseEntity(Errors errors) {
        this.code = errors.getCode();
        this.msg = errors.getMessage();
    }

    public ResponseEntity(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseEntity(T t) {
        this.code = SystemErrors.SUCCESS.code;
        this.msg = SystemErrors.SUCCESS.message;
        this.data = t;
    }
}
