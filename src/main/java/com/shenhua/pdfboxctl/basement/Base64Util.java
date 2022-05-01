package com.shenhua.pdfboxctl.basement;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @program: pdfboxctl
 * @Description: BufferedImage类型文件转base64格式，当前使用Base依赖为JDK11版本，如果使用JDK9以下，请自行百度改为sun包的依赖
 * @Author: Akio and xu,ziyun
 * @Create: 2022-05-01 13:51
 **/
@Slf4j
public class Base64Util {
    //
    public static String convertimgtoBase64(BufferedImage image) {
        String png_base64 = "";
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);//写入流中
            byte[] bytes = baos.toByteArray();//转换成字节
//            png_base64 = new BASE64Encoder().encode(bytes);//jdk1.8写法
            png_base64 = Base64.encodeBase64String(bytes);//JDK11写法
//        String png_base64 = Base64.encodeBase64String(bytes).trim();//转换成base64串
            png_base64 = png_base64.replaceAll("\n", "").replaceAll("\r", "");//删除 \r\n
        } catch (IOException e) {
//            e.printStackTrace();
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return png_base64;
    }

    public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
        if (base64 == null) { // 图像数据为空
            return false;
        }
//        BASE64Decoder decoder = new BASE64Decoder();//JDK1.8
        try {
            // Base64解码
//            byte[] bytes = decoder.decodeBuffer(base64);
            byte[] bytes = Base64.decodeBase64(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String imageToBase64(String path) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
        return Base64.encodeBase64String(data);
    }
}