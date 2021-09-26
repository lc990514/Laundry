package com.lyn521.pub.uploadfile.controller;

import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.uploadfile.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;

@RestController
@ResponseBody
@RequestMapping("/file")
public class UploadFileController {
    @Autowired
    private UploadFileService fileService;
    @Autowired(required = false)
    private HttpServletRequest request;
    @PostMapping("/uploadImage")
    public Object uploadImage(MultipartFile file){
        try {
            if (file.isEmpty()){
                return Result.error(MsgCode.FILE_NULL);
            }
            return fileService.uploadImage(file,request.getIntHeader("id"));
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(MsgCode.SERVER_ERROR);
        }
    }
    @PostMapping("/upload/image/base64")
    public Object uploadImageBase64(String file){
        try {
            return fileService.uploadImage(file,request.getIntHeader("id"));
        }catch (Exception e){
            e.printStackTrace();
            return Result.error(MsgCode.SERVER_ERROR);
        }
    }


    @GetMapping(value = "/image/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public Object getFile(@PathVariable long id) {
        try {
            FileInputStream inputStream = new FileInputStream(fileService.getFilePath(id));
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            inputStream.close();
            return bytes;
        }catch (Exception e){
            return null;
        }
    }
}
