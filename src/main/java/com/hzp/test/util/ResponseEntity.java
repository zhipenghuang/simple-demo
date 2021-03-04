package com.hzp.test.util;

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
    private int ecode = 0;

    @ApiModelProperty("请求结果信息提示")
    @JSONField(ordinal = 2)
    private String message;

    @ApiModelProperty("时间戳")
    @JSONField(ordinal = 3)
    private long ts;

    @ApiModelProperty("具体数据")
    @JSONField(ordinal = 4)
    private T data;

    public ResponseEntity() {
        this.ecode = SystemErrors.SUCCESS.code;
        this.message = SystemErrors.SUCCESS.message;
        this.ts = System.currentTimeMillis();
    }

    public ResponseEntity(Errors errors) {
        this.ecode = errors.getCode();
        this.message = errors.getMessage();
        this.ts = System.currentTimeMillis();
    }

    public ResponseEntity(int ecode, String message) {
        this.ecode = ecode;
        this.message = message;
        this.ts = System.currentTimeMillis();
    }

    public ResponseEntity(T t) {
        this.ecode = SystemErrors.SUCCESS.code;
        this.message = SystemErrors.SUCCESS.message;
        this.data = t;
        this.ts = System.currentTimeMillis();
    }
}
