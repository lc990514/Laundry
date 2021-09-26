package com.lyn521.pub.uploadfile.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface UploadFileService {
    Object uploadImage(MultipartFile file, int postId);
    public Object uploadImage( String file, int postId);

    File getFile(int id);

    String getFilePath(long id);
}
