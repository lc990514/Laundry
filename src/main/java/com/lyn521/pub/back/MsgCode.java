package com.lyn521.pub.back;

import lombok.Data;

@Data
public class MsgCode {
    private int code;
    private String message;

    public MsgCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static MsgCode SUCCESS = new MsgCode(0, "success");
    public static MsgCode SERVER_ERROR = new MsgCode(500, "服务器错误");
    //登录错误
    public static MsgCode USER_PASSWORD_ERROR = new MsgCode(500101, "用户名、密码或登录权限错误");
    public static MsgCode SESSION_ERROR = new MsgCode(500102, "登录失效，请重新登陆");
    public static MsgCode JURISDICTION_ERROR = new MsgCode(500103, "登录权限出错，请重新选择");
    //sql错误
    public static MsgCode DATA_NULL = new MsgCode(50003, "数据绑定错误");

    //跨域传参错误
    public static MsgCode NOT_GET_DATA = new MsgCode(500301, "数据传输错误");
    //图片上传错误
    public static MsgCode IMAGE_TYPE_ERROR = new MsgCode(500203,"请上传图片！");
    public static MsgCode IMAGE_SIZE_ERROR = new MsgCode(500203,"文件过大！");
    public static MsgCode SERVER_UPLOAD_ERROR = new MsgCode(500203,"文件上传错误！");
    public static MsgCode FILE_NULL = new MsgCode(500203,"文件为空！");

    //商品错误
    public static MsgCode SetMeal_NULL = new MsgCode(500401,"商品不存在或失效");
    public static MsgCode NUM_ERROR = new MsgCode(500402,"商品数量异常");
}
