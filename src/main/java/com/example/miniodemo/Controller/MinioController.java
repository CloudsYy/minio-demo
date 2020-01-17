package com.example.miniodemo.Controller;

import io.minio.MinioClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/minio")
public class MinioController {
    private static String url = "http://120.79.75.136:9000/";
    private static String accessKey = "minioadmin";
    private static String secretKey = "minioadmin";

    //上传文件到minio服务器
    @PostMapping("files")
    public String uploadFiles(@RequestParam("fileName") MultipartFile file) {
        try{
            MinioClient minioClient = new MinioClient(url, accessKey, secretKey);//minio客户端
            long l = System.currentTimeMillis();
            InputStream is = file.getInputStream();//获取文件流
            String filename = file.getOriginalFilename();//获取文件名称
            String contentType = file.getContentType();//获取文件类型
            minioClient.putObject("files",filename,is,contentType);//添加文件到(minio)存储桶中
            long l1 = System.currentTimeMillis();
            System.out.println("耗时多少："+(l1-l));
            System.out.println("is = " + is);
            System.out.println("filename = " + filename);
            System.out.println("contentType = " + contentType);
            return "上传成功！";
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "上传失败！";
        }
    }

    //下载文件
    @GetMapping("download")
    public void downloadFiles(){

    }
}