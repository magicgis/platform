package com.junl.wpwx.utils;

/*import com.qcloud.cos.common_utils.CommonCodecUtils;
import com.qcloud.cos.http.RequestBodyKey;
import com.qcloud.cos.http.RequestBodyValue;
import com.qcloud.cos.http.RequestHeaderKey;
import com.qcloud.cos.http.ResponseBodyKey;
import com.qcloud.cos.sign.Sign;
import net.sf.json.JSONObject;
import org.apache.log4j.spi.ErrorCode;

import java.io.FileInputStream;

public class text {*/

    /**
     * 单个文件上传,适用于小文件
     *
     * @param bucketName
     *            bucket名称
     * @param remotePath
     *            远程文件路径
     * @param localPath
     *            本地文件路径
     * @return 服务器端返回的操作结果，成员code为0表示成功，具体参照文档手册
     * @throws Exception
     */
  /*  public String uploadFile(String bucketName, String remotePath,
                             String localPath) throws Exception {
        if (!FileProcess.isLegalFile(localPath)) {
            String errorMsg = localPath
                    + " is not file or not exist or can't be read!";
            LOG.error(errorMsg);
            JSONObject errorRet = new JSONObject();
            errorRet.put(ResponseBodyKey.CODE, ErrorCode.PARAMS_ERROR);
            errorRet.put(ResponseBodyKey.MESSAGE, errorMsg);
            return errorRet.toString();
        }
        FileInputStream localFileInputStream = null;
        try {
            localFileInputStream = FileProcess.getFileInputStream(localPath);
            return uploadFile(bucketName, remotePath, localFileInputStream);
        } catch (Exception e) {
            LOG.error("UploadFile {} occur a error {}", localPath, e.toString());
            throw e;
        } finally {
            FileProcess.closeFileStream(localFileInputStream, localPath);
        }
    }*/





import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.common_utils.CommonCodecUtils;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

public class text
    {
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

        public static void main( String[] args )
        {
            // 创建 COSClient实例
          /*  String bucketName = "pic-1253415037";
            long appId =1253415037;
            String secretId = "AKIDxkIhsnMxB9tQipIRs40gNvEH6rRll77s";
            String secretKey = "d0KQ5XigQwFH5duxlIsFMFrCKBpCI7fw";*/
           // COSClient cosClient = new COSClient(appId, secretId, secretKey);
            COSClient cosClient = getCOSClient();
            String bucketName = "pic";
            // 要访问的URL
            String path = "https://www.baidu.com/img/bd_logo1.png";
            // 获取文件后缀
            String suffix = path.substring(path.lastIndexOf('.')+1);
            long currentTimeMillis = System.currentTimeMillis();
            // 已时间戳的形式命名, 保证文件不重复
            String localPath = "d://"+currentTimeMillis+"."+suffix;
            // 存储到COS 的路径
            String cosPath = "/haha.png";
            // 将文件存储到本地
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                File outFile = new File(localPath);
                OutputStream outputStream = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                while (true) {
                    int read = 0;
                    if (inputStream != null) {
                        read = inputStream.read(buf);
                    }
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(buf, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                // 文件上传到腾讯COS
                UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosPath, localPath);
                String str= cosClient.uploadFile(uploadFileRequest);
                System.out.println(str);
                // 判断本地文件是否存在, 如果存在则删除
                if(outFile.exists()){
                    outFile.delete();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 关闭COS连接
            cosClient.shutdown();
        }

        /**
         * 流文件上传，适用于小文件，自定义扩展方法
         *
         * @param bucketName
         *            bucket名称
         * @param remotePath
         *            远程文件路径
         * @param fileContent
         *            文件字节数组
         * @return 服务器端返回的操作结果，成员code为0表示成功，具体参照文档手册
         * @throws Exception
         */
   /* public String uploadFileExt(String bucketName, String remotePath,  byte[] fileContent) throws Exception {
        String url = getEncodedCosUrl(bucketName, remotePath);
        String shaDigest = CommonCodecUtils.getFileSha1(fileContent);


        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put(RequestBodyKey.OP, RequestBodyValue.OP_UPLOAD);
        postData.put(RequestBodyKey.SHA, shaDigest);
        long expired = getExpiredTimeInSec();
        String sign = Sign.appSignature(appId, secretId, secretKey, expired, bucketName);
        HashMap<String, String> httpHeader = new HashMap<String, String>();
        httpHeader.put(RequestHeaderKey.Authorization, sign);
        return httpSender.sendFileRequest(url, httpHeader, postData, fileContent, timeOut);
    }*/

    }

