package com.lyn521.pub.uploadfile.service;

import com.lyn521.pub.back.MsgCode;
import com.lyn521.pub.back.Result;
import com.lyn521.pub.tool.Base64ToFile;
import com.lyn521.pub.tool.SHA;
import com.lyn521.pub.uploadfile.mapper.UploadFileMapper;
import com.lyn521.pub.uploadfile.pojo.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired(required = false)
    private UploadFileMapper fileMapper;

    private static String uploadPathWindows;
    private static String uploadPathLinux;
    private static ConcurrentHashMap<Long,String> imgTmp=new ConcurrentHashMap<>();

    @Value(value = "${file.uploadPathWindows}")
    public void setUploadPathWindows(String upload) {
        uploadPathWindows = upload;
    }

    @Value(value = "${file.uploadPathLinux}")
    public void setUploadPathLinux(String path) {
        uploadPathLinux = path;
    }

    /**
     * 存储上传的文件的文件夹名称
     */
    public static final String UPLOAD_DIR = "/upload/image/";
    /**
     * 上传文件时允许上传的最大大小
     */
    public static final long UPLOAD_MAX_SIZE
            = 20 * 1024 * 1024;

    /**
     * 允许上传的文件类型的集合
     */
    public static final List<String> UPLOAD_CONTENT_TYPES
            = new ArrayList<String>();

    /**
     * 添加允许上传的文件类型
     */
    static {
        UPLOAD_CONTENT_TYPES.add(".jpeg");
        UPLOAD_CONTENT_TYPES.add(".jpg");
        UPLOAD_CONTENT_TYPES.add(".png");
        UPLOAD_CONTENT_TYPES.add(".gif");
        UPLOAD_CONTENT_TYPES.add(".bmp");
    }


    @Override
    public Object uploadImage( MultipartFile file, int postId) {
        String fileName=file.getOriginalFilename();
        String fileType=fileName.substring(fileName.lastIndexOf("."));
        if (!UPLOAD_CONTENT_TYPES.contains(fileType)) {
            return Result.error(MsgCode.IMAGE_TYPE_ERROR);
        }
        long size = file.getSize();
        if (size > UPLOAD_MAX_SIZE) {
            return Result.error(MsgCode.IMAGE_SIZE_ERROR);
        }
        // 确定上传的文件夹
        String parentPath = null;
        parentPath = getProjectRootPath()+UPLOAD_DIR;

        File parent = new File(parentPath);

        if (!parent.exists()) {
            parent.mkdirs();
        }
        // 确定上传的文件名
        String filename= SHA.md5(SHA.getDataMD5())+fileType;
        File dest = new File(parent, filename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return Result.error(MsgCode.SERVER_UPLOAD_ERROR);
        }
        UploadFile uploadFile=new UploadFile();

        uploadFile.setPostId(postId);
        uploadFile.setFile(UPLOAD_DIR+filename);
        int id=uploadFile(uploadFile);
        if (id==0){
            return Result.error(MsgCode.SERVER_UPLOAD_ERROR);
        }
        return Result.success(id);
    }
    @Override
    public Object uploadImage( String file, int postId) {
        MultipartFile f=null;
        try {
            f= Base64ToFile.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(MsgCode.FILE_NULL);
        }
        return uploadImage(f,postId);
    }


    @Override
    public File getFile(int id) {
        String path=imgTmp.get(id);
        if (path==null){
            UploadFile uploadFile=getFileId(id);
            path=getProjectRootPath()+uploadFile.getFile();
        }
        File file=new File(path);
        return file;
    }
    @Override
    public String getFilePath(long id) {
        String path=imgTmp.get(id);
        if (path==null){
            UploadFile uploadFile=getFileId(id);
            path=getProjectRootPath()+uploadFile.getFile();
        }
        return path;
    }
    public static String getProjectRootPath(){
        return uploadPathWindows;
    }
    public int uploadFile(UploadFile uploadFile){
        if (fileMapper.insert(uploadFile)>0){
            return uploadFile.getId();
        }
        return 0;
    }
    public UploadFile getFileId(long id){
        UploadFile uploadFile=fileMapper.selectById(id);
        String path=getProjectRootPath()+uploadFile.getFile();
        imgTmp.put(id,path);
        return uploadFile;
    }
}
