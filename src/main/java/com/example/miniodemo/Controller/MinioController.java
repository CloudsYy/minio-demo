package com.example.miniodemo.Controller;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
            long size = file.getSize();
            minioClient.putObject("files",filename,is,contentType);//添加文件到(minio)存储桶中
//            long l1 = System.currentTimeMillis();
//            System.out.println("耗时多少："+(l1-l));
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
    * 生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，
    * 即使其所在的存储桶是私有的。这个presigned URL可以设置一个失效时间，默认值是7天。
    * */
    @GetMapping("download")
    public String downloadFiles(@RequestParam("filename") String filename, HttpServletRequest request, HttpServletResponse httpResponse) throws InvalidPortException, InvalidEndpointException {
        MinioClient minioClient = new MinioClient(url,accessKey,secretKey);
        try(InputStream inputStream = minioClient.getObject("files",filename)) {
            // 调用statObject()来判断对象是否存在。
            // 如果不存在, statObject()抛出异常,
            // 否则则代表对象存在。
            minioClient.statObject("files", filename);
            if (filename.contains(".txt")){
//                String[] split = filename.split(".txt");
                String url = "C:\\Users\\Cloud\\Desktop\\FileFactory\\";
                File file = new File(url + filename);//文件路径
                if (file.exists()){
                    file.createNewFile();//创建文件
                }
                String path = file.getPath();
                minioClient.getObject("files",filename, path);
            }

//            long length = statObject.length();
//            String name = statObject.bucketName();
//            List<Bucket> buckets = minioClient.listBuckets();
//            for (Bucket bucket : buckets) {
//                System.out.println(bucket);
//            }
//          minioClient.makeBucket("bucket01","beijing",true); //生成桶
//          minioClient.removeBucket("bucket01");
          boolean files = minioClient.bucketExists("files");
          if (files) {
              System.out.println("文件存在！");
              String urlD = minioClient.presignedGetObject("files",filename);
              System.out.println("urlD = " + urlD);
              httpResponse.sendRedirect(urlD);
              return "下载成功！";
          } else {
              return "文件不存在！";
          }
        } catch (MinioException ex) {
            ex.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "下载失败！";
    }

}
