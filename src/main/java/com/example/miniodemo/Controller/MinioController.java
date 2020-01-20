package com.example.miniodemo.Controller;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

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
//            long l = System.currentTimeMillis();
            InputStream is = file.getInputStream();//获取文件流
            String filename = file.getOriginalFilename();//获取文件名称
            String contentType = file.getContentType();//获取文件类型
            minioClient.putObject("files",filename,is,contentType);//添加文件到(minio)存储桶中
//            long l1 = System.currentTimeMillis();
//            System.out.println("耗时多少："+(l1-l));
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

    /*
    * 下载文件
    * presignedGetObject(String bucketName, String objectName, Integer expires)
    * 生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
    * */
    @GetMapping("download")
    public String downloadFiles(@RequestParam String filename){
        try {
            MinioClient minioClient = new MinioClient(url,accessKey,secretKey);
//            List<Bucket> buckets = minioClient.listBuckets();
//            for (Bucket bucket : buckets) {
//                System.out.println(bucket);
//            }
//      minioClient.makeBucket("bucket01","beijing",true); 生成桶
//            minioClient.removeBucket("bucket01");
            String urlD = minioClient.presignedGetObject("files",filename);
            System.out.println("urlD = " + urlD);

            //返回下载链接
            return urlD;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "获取失败";
        }
    }
}