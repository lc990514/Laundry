package com.lyn521.pub.tool;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Base64ToFile implements MultipartFile {
    private final byte[] img;
    private final String head;

    public Base64ToFile(byte[] img, String head) {
        this.img = img;
        this.head = head;
    }

    @Override
    public String getName() {
        return SHA.getDataMD5()+"."+head.split("/")[1].split(";")[0];
    }

    @Override
    public String getOriginalFilename() {
        return SHA.getDataMD5()+"."+head.split("/")[1].split(";")[0];
    }

    @Override
    public String getContentType() {
        return head.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return img==null || img.length==0;
    }

    @Override
    public long getSize() {
        return img.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return img;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(img);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
       new  FileOutputStream(file).write(img);

    }
    public static MultipartFile parse(String base64) throws IOException {
        String[] base = base64.split(",");
        byte[] b=new byte[0];
        b= Base64.decodeBase64(base[1].getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < b.length; i++) {
            if (b[i]<0){
                b[i]+=256;
            }
        }
        return new Base64ToFile(b,base[0]);
    }
}
