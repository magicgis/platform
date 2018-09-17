package com.junl.wpwx.utils;
import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.GetFileLocalRequest;
import com.qcloud.cos.request.MoveFileRequest;
import com.qcloud.cos.request.StatFileRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

import java.io.File;

/**
 *
 * @ClassName: QcloudDemo
 * @Description: 测试云储存
 * @author chengrui
 * @date 2017年7月25日 下午2:59:17
 */
public class QcloudDemo {

    /**
     *
     * @Title: getCOSClient
     * @Description: 生成客户端对象
     * @return
     */
    public static COSClient getCOSClient() {

        // 初始化秘钥信息
        //String bucketName = "pic-1253415037";
        long appId =1253415037;
        String secretId = "AKIDxkIhsnMxB9tQipIRs40gNvEH6rRll77s";
        String secretKey = "d0KQ5XigQwFH5duxlIsFMFrCKBpCI7fw";
        /*long appId = 126;
        String secretId = "AKIDobGq4Xca";
        String secretKey = "vgGQp3dD";*/
        // 设置要操作的bucket
        // String bucketName = "goods";
        // 初始化秘钥信息
        Credentials cred = new Credentials(appId, secretId, secretKey);

        // 初始化客户端配置(如设置园区)
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        // 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
        clientConfig.setRegion("sh");

        // 生成客户端
        // 初始化cosClient
        COSClient cosClient = new COSClient(clientConfig, cred);

        return cosClient;
    }

    /**
     *
     * @Title: uploadFile
     * @Description:上传文件
     */
    public static String uploadFile() {

        // 设置要操作的bucket
        String bucketName = "pic1";
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, "/2018.jpg", "D:\\2018.jpg");
        String uploadFileRet = getCOSClient().uploadFile(uploadFileRequest);

        return uploadFileRet;
    }

    /**
     *
     * @Title: downFile
     * @Description: 下载文件
     * @return
     */
    public static String downFile() {
        // 设置要操作的bucket
        String bucketName = "goods";
        String cosFilePath = "/demo1.jpg";
        String localPathDown = "src/demo1.jpg";
        GetFileLocalRequest getFileLocalRequest = new GetFileLocalRequest(bucketName, "/2018.jpg", "D:\\2018.jpg");
        getFileLocalRequest.setUseCDN(false);
        getFileLocalRequest.setReferer("*.myweb.cn");
        String getFileResult = getCOSClient().getFileLocal(getFileLocalRequest);
        return getFileResult;
    }

    /**
     *
     * @Title: moveFile
     * @Description: 移动文件
     * @return
     */
    public static String moveFile() {
        // 设置要操作的bucket
        String bucketName = "goods";

        String cosFilePath = "/demo/demo1.jpg";
        String dstCosFilePath = "/demo1.jpg";
        MoveFileRequest moveRequest = new MoveFileRequest(bucketName, cosFilePath, dstCosFilePath);
        String moveFileResult = getCOSClient().moveFile(moveRequest);

        return moveFileResult;
    }

    /**
     *
     * @Title: getFileProp
     * @Description: 获取文件
     * @return
     */
    public static String getFileProp() {
        // 设置要操作的bucket
        String bucketName = "pic";

        StatFileRequest statFileRequest = new StatFileRequest(bucketName, "/2018.jpg");
        String statFileRet = getCOSClient().statFile(statFileRequest);

        return statFileRet;
    }

    /**
     *
     * @Title: deleteFile
     * @Description: 删除文件
     * @return
     */
    public static String deleteFile() {
        // 设置要操作的bucket
        String bucketName = "goods";

        DelFileRequest delFileRequest = new DelFileRequest(bucketName, "/demo1.jpg");
        String delFileRet = getCOSClient().delFile(delFileRequest);

        return delFileRet;
    }

    /**
     *
     * @Title: uploadFile
     * @Description:上传和获取文件
     */
    public static String getUpFile(String cosFilePath,String dstCosFilePath) {
        // 设置要操作的bucket
        String bucketName = "pic";
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosFilePath, dstCosFilePath);
         getCOSClient().uploadFile(uploadFileRequest);
        StatFileRequest statFileRequest = new StatFileRequest(bucketName, cosFilePath);
        String statFileRet = getCOSClient().statFile(statFileRequest);
        FileRet fileRet = JSON.parseObject(statFileRet, FileRet.class);
        DataRet data =  fileRet.getData();
        return data.getSource_url();
    }

    public static void main(String[] args) {
         //System.out.println(uploadFile());
         //System.out.println(downFile());
        // System.out.println(moveFile());
        //System.out.println(getFileProp());
        //System.out.println(deleteFile());
        System.out.println(getUpFile("/2018.jpg","D:\\2018.jpg"));

    }

}