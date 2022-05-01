package com.shenhua.pdfboxctl.basement;


import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * @program: pdfboxctl
 * @Description: 封装PDFBox的截取提取区域图片文字的方法
 * @Author: Akio and xu,ziyun
 * @Create: 2022-05-01 15:24
 **/

/**
 * notice:
 * 这个类中的pdfDoc.getPage();中下标是从0开始
 * 目前所传入的nowPage是标准页面，下标从1开始，所以对于pdfDoc.getPage(nowPage-1)需要减一
 * <p>
 * PDFTextStripper类确定的下标是从1开始的
 * 所以对于以上两者的结合使用，需要注意
 */
@Slf4j
public class PdfboxUtils {
    public static final String REGION_NAME = "content";

    /**
     * 根据指定文件页码的指定区域读取文字
     *
     * @param file      文件
     * @param nowPage   PDF页码
     * @param textRrect 读取文字的区域
     * @return 文字内容
     */
    public static String readRectangelText(File file, int nowPage, Rectangle textRrect) {

        String textContent = "";

        try (PDDocument pdfDoc = PDDocument.load(file)) {
            // 获取指定的PDF页
            PDPage pdfPage = pdfDoc.getPage(nowPage - 1);

            // 获取指定位置的文字（区域文字剥离器）
            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
            textStripper.setSortByPosition(true);
            //添加一个新区域以对文本进行分组,REGION_NAME为arraylist的key
            textStripper.addRegion(REGION_NAME, textRrect);
            //处理指定页提取文本
            textStripper.extractRegions(pdfPage);
            //
            textContent = textStripper.getTextForRegion(REGION_NAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return textContent;

    }

    public static String readPageText(File file, int nowPage) {
        String textContent = "";

        try (PDDocument pdfDoc = PDDocument.load(file)) {
            // 获取指定的PDF页
            PDPage pdfPage = pdfDoc.getPage(nowPage - 1);
            //普通文字剥离器
            PDFTextStripper stripper = new PDFTextStripper();
            // 设置是否排序
            stripper.setSortByPosition(true);
            // 设置起始页
            stripper.setStartPage(nowPage);
            // 设置结束页
            stripper.setEndPage(nowPage);
            textContent = stripper.getText(pdfDoc);
//            System.out.println(+"---");

            //PDDocument 切割器
//            Splitter splitter = new Splitter();
//            splitter.setStartPage(nowPage);
//            splitter.setEndPage(nowPage+1);
//            List<PDDocument> pagesOfDoc = splitter.split(pdfDoc);
//            System.out.println();

//            // 获取指定位置的文字（文字剥离器）
//            PDFTextStripperByArea textStripper = new PDFTextStripperByArea();
//            textStripper.setSortByPosition(true);
//            //添加一个新区域以对文本进行分组,REGION_NAME为harraylist的key
//            textStripper.addRegion(REGION_NAME, textRrect);
//            //处理指定页提取文本
//            textStripper.extractRegions(pdfPage);
//            //
//            textContent = textStripper.getTextForRegion(REGION_NAME);
//            System.out.println(textStripper.getText(pdfDoc));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return textContent;
    }

    /**
     * 根据指定文件页码的指定区域读取图片
     *
     * @param file     PDF文件
     * @param nowPage  PDF页码
     * @param imgRrect 读取图片的区域
     * @return 图片内容
     */
    public static BufferedImage readRectangelImage(File file, int nowPage, Rectangle imgRrect) {
        BufferedImage bufImage = null;
        try (PDDocument pdfDoc = PDDocument.load(file)) {
            // 获取渲染器，主要用来后面获取BufferedImage
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDoc);
            // 截取指定位置生产图片
//            bufImage = pdfRenderer.renderImage(nowPage - 1,20f).getSubimage(imgRrect.x, imgRrect.y, imgRrect.width, imgRrect.height);
            // 这里调整dpi是生成到本地图片的清晰度
//            bufImage = pdfRenderer.renderImageWithDPI(nowPage - 1, 432f, ImageType.RGB).getSubimage(imgRrect.x * 6, imgRrect.y * 6, imgRrect.width * 6, imgRrect.height * 6);
            bufImage = pdfRenderer.renderImageWithDPI(nowPage - 1, 144f, ImageType.RGB).getSubimage(imgRrect.x * 2, imgRrect.y * 2, imgRrect.width * 2, imgRrect.height * 2);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage());
        }
        return bufImage;
    }
}

