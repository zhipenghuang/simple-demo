package com.hzp.test.exception;

/**
 * @author yunmu
 * @create 2018/9/19.
 */
public enum SystemErrors implements Errors {

    SUCCESS(0, "success"),
    FAIL(-1, "failure"),
    SYSTEM_ERROR(500, "内部服务错误"),
    REQUEST_BASE_EXCEPTION(1001, "请求异常"),

    //自定义
    ID_NOT_NULL(2001, "id不能为空"),
    PASSWORD_ERROR(2002, "密码错误"),
    GROUP_WECHAT_NOT_NULL(2003, "组名和微信号都不能为空"),
    GROUP_EXISTS(2004, "组名已存在"),
    NO_DOMAIN(2005, "域名没有配置"),
    PASSWORD_NOT_EXISTS(2006, "密码未设置"),
    CN_CHAR_EXISTS(2007, "不能使用中文逗号"),
    TOKEN_ERROR(2008, "token不合法");


    public int code;
    public String message;

    SystemErrors(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public static String getMsg(int code) {
        for (SystemErrors errorCode : SystemErrors.values()) {
            if (code == errorCode.getCode()) {
                return errorCode.getMessage();
            }
        }
        return null;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String msg) {
        this.message = msg;
    }
}
