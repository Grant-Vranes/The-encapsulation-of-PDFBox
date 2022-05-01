package com.shenhua.pdfboxctl.pdfBoxCtl;

import com.shenhua.pdfboxctl.basement.BoxKeyPosition;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * @program: pdfboxctl
 * @Description: PDFBox基本方法
 * @Author: Akio
 * @Create: 2022-05-01 14:56
 **/
public class BaseMethod {
    /**
     * 获取PDF文件页数
     * Get the number of pages of the PDF file
     * @param file
     * @return
     */
    public static Integer getPages(File file) {
        Integer pages = null;
        try (
                PDDocument doc = PDDocument.load(file);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))
        ) {
            //transfer to byte array
            byte[] fileBytes = new byte[bis.available()];
            bis.read(fileBytes);

            BoxKeyPosition pdf = new BoxKeyPosition(fileBytes);
            pages = pdf.getAllPage();//get all of position from keywords
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pages;
    }


}
