package com.lyn521.pub.back;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String message;
    private Object data;

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(Object date){
        MsgCode msgCode = MsgCode.SUCCESS;
        return  new  Result(msgCode.getCode(), msgCode.getMessage(),date);
    }
    public static Result error(MsgCode msgCode){
        return  new  Result(msgCode.getCode(), msgCode.getMessage(),null);
    }
}
