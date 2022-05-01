package com.shenhua.pdfboxctl.basement;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @program: pdfboxctl
 * @Description: 基本方法
 * @Author: Akio
 * @Create: 2022-05-01 13:59
 **/
public class BaseTools {

    /**
     * MultipartFile文件转File文件，对接前端传递的MultipartFile文件数据
     * MultipartFileToFile
     * 切记，用完之后要调用deleteTempFile删除临时文件
     *
     * @param multiFile
     * @return
     */
    public static File MultipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若须要防止生成的临时文件重复,能够在文件名后添加随机码
        File file = null;
        try {
            file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除临时文件
     *
     * @param file
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File tempFile = new File(file.toURI());
            tempFile.delete();
        }
    }

    /*public static MultipartFile getMultipartFile(File file) {
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }*/

}
