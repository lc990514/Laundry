package com.lyn521.pub.uploadfile.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_files")
public class UploadFile {
    private int id;
    private String file;
    private int postId;
}
