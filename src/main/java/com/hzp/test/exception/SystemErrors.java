package com.hzp.test.exception;

/**
 * @author yunmu
 * @create 2018/9/19.
 */
public enum SystemErrors implements Errors {

    SUCCESS(0, "success"),
    FAIL(-1, "failure"),

    SYSTEM_ERROR(500, "内部服务错误"),

    SERVLET_EXCEPTION(402, "请求处理出现异常"),

    METHOD_ARGUMENT_TYPE_MISMATCH_EXCEPTION(4004, "参数绑定异常！"),
    REQUEST_PARAM_ERROR(4005, "请求参数格式或绑定错误"),
    //自定义
    ID_NOT_NULL(1001, "id不能为空"),
    PASSWORD_ERROR(1002, "密码错误"),
    GROUP_WECHAT_NOT_NULL(1003, "组名和微信号都不能为空"),
    GROUP_EXISTS(1004, "组名已存在"),
    NO_DOMAIN(1005, "域名没有配置"),
    PASSWORD_NOT_EXISTS(1006, "密码未设置"),
    CN_CHAR_EXISTS(1007, "不能使用中文逗号");


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
