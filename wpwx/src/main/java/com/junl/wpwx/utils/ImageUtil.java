package com.junl.wpwx.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Date;
import java.util.Random;


public class ImageUtil {


    public static String getRandomInt(int length) { //length表示生成字符串的长度
        String base = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    // 图片转化成base64字符串  // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    public static  String GetImageStr( String imgFile) {
        InputStream in = null;
        byte[] data = null; // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();  // 对字节数组Base64编码
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    // base64字符串转化成图片 // 对字节数组字符串进行Base64解码并生成图片
    // imgStr：转换的bs64编码 imgPath：图片存储的地址 format ：转换的图片格式
    // return 生成图片的名称
    public static  String GenerateImage(String imgStr,String imgPath ,String format) {
        if (imgStr == null) // 图像数据为空
            return  null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);  // Base64解码
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成peg图片
            String date=String.valueOf(new Date().getTime()).substring(8,13)+"_"+getRandomInt(6);
            String imgFilePath = imgPath +date+"."+format;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return date;
        } catch (Exception e) {
        	System.out.println(e);
            return "";
        }
    }

    /*public static void main(String[] args) {
        String imgFile = "D:/Program Files (x86)/nginx-1.13.7/html/pic/logo.jpg";// 待处理的图片 D:/Program Files (x86)/nginx-1.13.7/html/pic/logo.jpg
        String strImg = GetImageStr(imgFile); //图片装bs64
       // System.out.println(strImg);
        String mc= GenerateImage(strImg,"D:/Program Files (x86)/nginx-1.13.7/html/avatar/","jpg"); //bs64装图片
        System.out.println(mc);
    }*/

    }

